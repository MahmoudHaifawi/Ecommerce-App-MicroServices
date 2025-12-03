package com.micro.order.payment;



import com.micro.order.config.FeignAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * PaymentClient
 * -------------
 * Feign interface for sending payment requests from the Order microservice to the Payment microservice.
 *
 * <p>
 * <b>How it works:</b>
 * <ul>
 *     <li>This interface uses Feign (Spring Cloud OpenFeign) to simplify HTTP communication.
 *     <li>Feign automatically generates implementations for REST calls at runtime, so no boilerplate HTTP client code is needed.</li>
 *     <li>The <code>@FeignClient</code> annotation pulls the target URL from the order microservice’s configuration:
 *         <ul>
 *             <li><code>${application.config.payment-url}</code> in <code>application.yml</code></li>
 *             <li>In your setup, this resolves to <code>http://localhost:8222/api/v1/payments</code></li>
 *         </ul>
 *     </li>
 *     <li>The <code>name</code> parameter should reflect service purpose (“payment-service” is recommended for clarity).</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Usage scenario:</b>
 * <ul>
 *     <li>Order microservice calls <code>requestOrderPayment()</code> when a new order is created and needs payment processing.</li>
 *     <li>Method posts <code>PaymentRequest</code> to <code>/api/v1/payments</code> endpoint of the Payment microservice.</li>
 *     <li>Returns the payment ID or status as <code>Integer</code>.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Configuration:</b>
 * <ul>
 *     <li><code>application.config.payment-url</code> must be set in the order microservice’s config file.</li>
 *     <li>Does NOT read properties from the Payment microservice—each microservice always reads its own config.</li>
 *     <li>Can run with or without Eureka/service discovery, but the <code>name</code> is still best practice.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Benefits:</b>
 * <ul>
 *     <li>Clean separation of microservice concerns (order logic vs payment logic).</li>
 *     <li>Declarative REST client—no manual serialization, error handling, or RESTTemplate code needed.</li>
 *     <li>Easy to change API endpoints and microservice URLs without code change (just config update).</li>
 * </ul>
 * </p>
 *
 */
@FeignClient(
        name = "payment-service", // Logical name for easier tracing and possible service discovery
        url = "${application.config.payment-url}", // Base URL set in order service config file
        configuration = FeignAuthConfig .class       // ⬅️ forward Authorization header
)
public interface PaymentClient {

    /**
     * Initiates a payment request for a new order.
     * <p>
     * Sends a POST request to the Payment microservice’s /api/v1/payments endpoint, passing
     * all required payment and order data. Returns the resulting payment transaction’s ID.
     * Feign will serialize the PaymentRequest as JSON automatically.
     *
     * @param request The payment request, containing amount, method, customer, and order reference.
     * @return The new payment’s ID, or a status code as Integer.
     */
    @PostMapping // Uses the base config URL mapped to /api/v1/payments
    Integer requestOrderPayment(@RequestBody PaymentRequest request);
}
