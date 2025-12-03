package com.micro.customer.customer;

/**
 * Data Transfer Object (DTO) for sending customer information in API responses.
 * <p>
 * Defined as a Java record to provide an immutable, concise data structure for exposing customer details
 * without additional behavior or mutability.
 * <ul>
 *   <li>Records auto-generate a constructor, getters, equals, hashCode, and toString methods.</li>
 *   <li>Ensures that response data is immutable and thread-safe.</li>
 *   <li>Used for transferring data from the server to clients (e.g., REST API responses).</li>
 * </ul>
 * Avoids the boilerplate code required by traditional classes with getters and setters.
 */
public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        Address address
) {}



