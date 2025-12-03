package com.micro.order.kafka;

import com.micro.order.customer.CustomerResponse;
import com.micro.order.order.PaymentMethod;
import com.micro.order.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation (
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products

) {
}