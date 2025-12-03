package com.micro.customer.customer;

import org.springframework.stereotype.Component;


/**
 * Maps between different customer-related data representations.
 * <p>
 * This component provides methods for converting data transfer objects (DTOs):
 * <ul>
 *   <li><b>toCustomer:</b> Converts a {@link CustomerRequest} DTO to a {@link Customer} entity.
 *   Used for creating new customers from client API requests. Copies relevant properties using the builder pattern.</li>
 *   <li><b>fromCustomer:</b> Converts a {@link Customer} entity to a {@link CustomerResponse} DTO.
 *   Used for sending customer data to clients in API responses. Copies all appropriate fields.</li>
 * </ul>
 * Having a mapping layer centralizes conversion logic, ensuring request/response objects are
 * decoupled from internal entity models. This improves code maintainability and testability.
 * <p>
 * This class is annotated with {@code @Component} so that it can be injected and managed by Spring's dependency injection.
 */
@Component
public class CustomerMapper {

    /**
     * Converts a CustomerRequest DTO into a Customer entity.
     * Returns {@code null} if the input is {@code null}.
     */
    public Customer toCustomer(CustomerRequest request) {
        if (request == null) {
            return null;
        }
        return Customer.builder()
                .id(request.id())
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .address(request.address())
                .build();
    }

    /**
     * Converts a Customer entity into a CustomerResponse DTO.
     * Returns {@code null} if the input is {@code null}.
     */
    public CustomerResponse fromCustomer(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress()
        );
    }
}


