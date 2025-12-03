package com.micro.customer.customer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Why was @RequiredArgsConstructor used?
 *
 * The @RequiredArgsConstructor annotation from Lombok generates a constructor with required arguments (i.e., for all final fields and fields marked as @NonNull) automatically.

 * In this class, there is a single dependency: private final CustomerService service;
 * That means Lombok will generate a constructor that takes a CustomerService parameter,
 * and Spring can use this constructor to inject the dependency, supporting constructor-based dependency injection —
 * which is recommended as it makes classes easier to test and maintain.
 *
 * */
/**
 * REST controller for managing customer-related operations in the e-commerce system.
 * <p>
 * Defines HTTP endpoints to create, update, fetch, verify existence, and delete customers.
 * Utilizes {@link CustomerService} for business logic and delegation.
 * <ul>
 *   <li>All endpoints are under the path: {@code /api/v1/customers}</li>
 *   <li>Uses Jakarta Bean Validation to validate request bodies where appropriate</li>
 *   <li>Embraces RESTful practices in request mapping and status code selection</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    /**
     * Creates a new customer record.
     * <p>
     * Handles HTTP POST requests to {@code /api/v1/customers}. Expects a valid {@link CustomerRequest} object
     * in the request body. The incoming request is validated using Jakarta Bean Validation annotations.
     * If validation passes, delegates to {@link CustomerService#createCustomer(CustomerRequest)}
     * to perform the creation logic and returns a confirmation message.
     * <ul>
     *   <li><b>Request Body:</b> {@link CustomerRequest} (validated automatically with {@code @Valid})</li>
     *   <li><b>Response:</b> {@code 200 OK} with a success message</li>
     *   <li><b>Validation:</b> Any input validation errors are handled by the framework and result in
     *   an error response before the method is invoked.</li>
     * </ul>
     *
     * @param request the validated customer creation request data
     * @return success message as {@link ResponseEntity}
     */
    @PostMapping
    public ResponseEntity<String> createCustomer(
            @RequestBody @Valid CustomerRequest request
    ) {
        return ResponseEntity.ok(this.customerService.createCustomer(request));
    }

    /**
     * Updates an existing customer record.
     * <p>
     * Handles HTTP PUT requests to {@code /api/v1/customers}. Expects a valid {@link CustomerRequest} object with the updated details.
     * The request is validated using Jakarta Bean Validation annotations. If validation passes, delegates to
     * {@link CustomerService#updateCustomer(CustomerRequest)} to perform the update logic.
     * The method responds with a 202 Accepted status, indicating the request was received and will be processed (possibly asynchronously).
     * <ul>
     *   <li><b>Request Body:</b> {@link CustomerRequest} (validated automatically with {@code @Valid})</li>
     *   <li><b>Response:</b> {@code 202 Accepted} (no content)</li>
     *   <li><b>Validation:</b> Any input validation errors trigger an error response before the method runs.</li>
     * </ul>
     *
     * @param request the validated customer update request data
     * @return empty response entity with status 202 Accepted
     */
    @PutMapping
    public ResponseEntity<Void> updateCustomer(
            @RequestBody @Valid CustomerRequest request
    ) {
        this.customerService.updateCustomer(request);
        return ResponseEntity.accepted().build();
    }

    /**
     * Retrieves all customers.
     * <p>
     * Handles HTTP GET requests to {@code /api/v1/customers}. Returns a list of all customers present in the system,
     * converted to {@link CustomerResponse} DTOs for safe client exposure.
     * <ul>
     *   <li><b>Response:</b> {@code 200 OK} with a list of customers</li>
     *   <li><b>Returns:</b> List of {@link CustomerResponse} objects in the HTTP response body</li>
     * </ul>
     *
     * @return list of all customers as {@link ResponseEntity}
     */
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll() {
        return ResponseEntity.ok(this.customerService.findAllCustomers());
    }

    /**
     * Checks existence of a customer by ID.
     * <p>
     * Handles HTTP GET requests to {@code /api/v1/customers/exists/{customer-id}}.
     * Delegates to {@link CustomerService#existsById(String)} to determine if a customer exists in the database.
     * <ul>
     *   <li><b>Path Variable:</b> {@code customer-id} specifies the ID to check for existence</li>
     *   <li><b>Response:</b> {@code 200 OK} with a boolean indicating existence</li>
     * </ul>
     *
     * @param customerId the ID of the customer to check
     * @return {@code true} if the customer exists, {@code false} otherwise
     */
    @GetMapping("/exists/{customer-id}")
    public ResponseEntity<Boolean> existsById(
            @PathVariable("customer-id") String customerId
    ) {
        return ResponseEntity.ok(this.customerService.existsById(customerId));
    }

    /**
     * Finds a customer by ID.
     * <p>
     * Handles HTTP GET requests to {@code /api/v1/customers/{customer-id}}.
     * Retrieves customer details for the specified ID and converts them to a {@link CustomerResponse} DTO.
     * If no such customer exists, a 404 Not Found error is typically triggered by the service layer.
     * <ul>
     *   <li><b>Path Variable:</b> {@code customer-id} specifies which customer to retrieve</li>
     *   <li><b>Response:</b> {@code 200 OK} with customer data, or 404 if not found</li>
     * </ul>
     *
     * @param customerId ID of the customer to retrieve
     * @return customer details as a {@link CustomerResponse}
     */
    @GetMapping("/{customer-id}")
    public ResponseEntity<CustomerResponse> findById(
            @PathVariable("customer-id") String customerId
    ) {
        return ResponseEntity.ok(this.customerService.findById(customerId));
    }

    /**
     * Deletes a customer by ID.
     * <p>
     * Handles HTTP DELETE requests to {@code /api/v1/customers/{customer-id}}.
     * Delegates to {@link CustomerService#deleteCustomer(String)} for removal logic.
     * The method replies with 202 Accepted to signal that the delete operation has been received and will be processed.
     * <ul>
     *   <li><b>Path Variable:</b> {@code customer-id} specifies which customer to delete</li>
     *   <li><b>Response:</b> {@code 202 Accepted} (no content)</li>
     * </ul>
     *
     * @param customerId ID of the customer to remove
     * @return empty response entity with status 202 Accepted
     */
    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("customer-id") String customerId
    ) {
        this.customerService.deleteCustomer(customerId);
        return ResponseEntity.accepted().build();
    }

}




