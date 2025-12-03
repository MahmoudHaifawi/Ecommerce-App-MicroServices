package com.micro.customer.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;


/**
 * Request payload for customer data creation or update.
 * <p>
 * Validation annotations are placed directly on fields to ensure that incoming data fulfills
 * important constraints before being processed by the application.
 * <ul>
 *   <li>{@code @NotNull} ensures required fields are present and not null.</li>
 *   <li>{@code @Email} validates that email field must be a properly formatted email address.</li>
 * </ul>
 * Placing validation in the record itself enables automatic request validation
 * by frameworks like Spring or Jakarta EE. If constraints are violated,
 * errors are generated before business logic is executed, enforcing data integrity early.
 */

public record CustomerRequest(
        String id,
        @NotNull(message = "Customer firstname is required")
        String firstname,
        @NotNull(message = "Customer lastname is required")
        String lastname,
        @NotNull(message = "Customer Email is required")
        @Email(message = "Customer Email is not a valid email address")
        String email,
        Address address
) {}
