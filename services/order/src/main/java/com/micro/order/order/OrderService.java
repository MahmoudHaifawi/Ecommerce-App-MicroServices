package com.micro.order.order;

import com.micro.order.customer.CustomerClient;
import com.micro.order.excpetis.BusinessException;
import com.micro.order.kafka.OrderConfirmation;
import com.micro.order.kafka.OrderProducer;
import com.micro.order.orderLine.OrderLineRequest;
import com.micro.order.orderLine.OrderLineService;
import com.micro.order.payment.PaymentClient;

import com.micro.order.payment.PaymentRequest;
import com.micro.order.product.ProductClient;
import com.micro.order.product.PurchaseRequest;
import jakarta.validation.Valid;
import java.util.List;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final CustomerClient customerClient;
    private final PaymentClient paymentClient;
    private final ProductClient productClient;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    /**
     * Creates a new order by performing the complete order workflow.
     * <p>
     * Steps performed:
     * <ol>
     *     <li><b>Customer Validation:</b> Checks if the customer exists using the CustomerClient.
     *         Throws BusinessException if not found (business rule violation).</li>
     *     <li><b>Product Purchase:</b> Requests the ProductClient to attempt purchasing the requested products.
     *         If products are unavailable, the operation fails here.</li>
     *     <li><b>Order Persistence:</b> Converts the incoming OrderRequest to an Order entity using the OrderMapper,
     *         then saves it to the database and gets the persisted Order object.</li>
     *     <li><b>Order Lines Creation:</b> For each purchased product, creates and saves an OrderLine
     *         representing the ordered item and its quantity through the OrderLineService.</li>
     *     <li><b>Payment Request:</b> Builds a PaymentRequest object for the order—includes amount, method, order details, and customer.
     *         Sends this request to the PaymentClient to process payment.</li>
     *     <li><b>Order Confirmation Event:</b> Constructs an OrderConfirmation event containing order, customer, payment,
     *         and product details. Uses the OrderProducer to publish the event to the Kafka message broker for downstream processing.</li>
     * </ol>
     * <p>
     * All steps are performed in a transaction—if any step fails (product not available, payment failed, etc.),
     * the transaction is rolled back and no partial data is persisted.
     *
     * @param request the OrderRequest DTO containing order, product, and customer info.
     * @return the ID of the newly created order if successful.
     * @throws BusinessException if customer does not exist, products cannot be purchased, or payment fails.
     */
    @Transactional
    public Integer createOrder(OrderRequest request) {
        // 1. Validate customer existence
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided ID"));

        // 2. Attempt to purchase requested products (transactional safety)
        var purchasedProducts = productClient.purchaseProducts(request.products());

        // 3. Convert order DTO to entity and save to DB
        var order = this.repository.save(mapper.toOrder(request));

        // 4. For each purchased product, create and save an OrderLine for tracking items in the order
        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        // 5. Build and send a payment request
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        // 6. Build and publish an order confirmation event to Kafka
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );

        // Return generated order ID
        return order.getId();
    }


    public List<OrderResponse> findAllOrders() {
        return this.repository.findAll()
                .stream()
                .map(this.mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer id) {
        return this.repository.findById(id)
                .map(this.mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided ID: %d", id)));
    }
}