package com.micro.order.customer;

import com.micro.order.config.FeignAuthConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

/**
 * CustomerClient
 * --------------
 * This interface defines a Feign client for communication between the order service and the customer service.
 *
 * Why do we need it?
 * - In a microservices architecture, each service is independent. The order service must fetch customer details
 *   (for validation or displaying info) when an order is processed, but those details belong to the customer service.
 *
 * What does it do?
 * - Uses Spring Cloud OpenFeign to declare a REST client.
 * - Feign automatically generates the boilerplate HTTP logic under the hood—no need to manually write REST calls.
 * - Allows simple use in Java code: inject this interface, call findCustomerById(), and Feign handles the HTTP request.
 *
 * Configuration:
 * - The actual customer service endpoint is injected from application properties using the 'url' parameter.
 * - 'name' is an internal identifier for the Feign client (helpful for logging, debugging).
 */
@FeignClient(
        name = "customer-service",
        url = "${application.config.customer-url}",   // http://localhost:8222/api/v1/customers
        configuration = FeignAuthConfig.class
)
public interface CustomerClient {

    /**
     * Calls the customer service's GET /{customer-id} endpoint to fetch customer info by ID.
     * @param customerId The unique identifier of the customer to retrieve.
     * @return Optional<CustomerResponse> containing data if found, empty if not.
     *
     * Process:
     * - The order service calls this method as part of processing a new order.
     * - Feign makes an HTTP GET request to customer-service/{customer-id}.
     * - Response is parsed into a CustomerResponse object.
     */
    @GetMapping("/{customer-id}")
     Optional<CustomerResponse> findCustomerById(@PathVariable("customer-id") String customerId);

}
