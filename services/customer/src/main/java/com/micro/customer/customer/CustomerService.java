package com.micro.customer.customer;

import com.micro.customer.customer.exception.CustomerNotFoundException;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
/**
 * Service layer for managing customer operations in the ecommerce system.
 * <p>
 * This class encapsulates the business logic for creating, updating, retrieving,
 * and deleting customer records. It relies on the injected {@link CustomerRepository}
 * for data persistence and {@link CustomerMapper} for mapping between
 * entities and data transfer objects (DTOs).
 * </p>
 * <p>
 * <b>Annotations:</b>
 * <ul>
 *   <li>{@code @Service} – Indicates this is a Spring-managed service component.</li>
 *   <li>{@code @RequiredArgsConstructor} (Lombok) – Automatically generates a constructor
 *       for all {@code final} fields to support dependency injection.</li>
 * </ul>
 * </p>
 *
 * <b>Key Methods:</b>
 * <ul>
 *   <li>{@link #createCustomer(CustomerRequest)} – Saves a new customer and returns its ID.</li>
 *   <li>{@link #updateCustomer(CustomerRequest)} – Updates a customer, throws if not found.</li>
 *   <li>{@link #findAllCustomers()} – Retrieves all customers as response DTOs.</li>
 *   <li>{@link #findById(String)} – Returns a customer by ID or throws if not found.</li>
 *   <li>{@link #existsById(String)} – Checks if a customer with this ID exists.</li>
 *   <li>{@link #deleteCustomer(String)} – Deletes a customer by ID.</li>
 * </ul>
 *
 * <b>Exception Handling:</b>
 * <ul>
 *   <li>Throws {@link com.alibou.ecommerce.exception.CustomerNotFoundException} whenever
 *       a customer cannot be found for update or retrieval operations.</li>
 * </ul>
 *
 * <b>Thread Safety:</b>
 * <p>
 * This class is stateless and safe for concurrent use, as all dependencies are themselves
 * thread-safe within the Spring ecosystem.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CustomerService
{

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    /**
     * Creates and persists a new customer based on request data.
     *
     * @param request a {@link CustomerRequest} DTO containing new customer information
     * @return the ID of the newly created customer
     */
    public String createCustomer(CustomerRequest request) {
        var customer = this.repository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    /**
     * Updates the details of an existing customer.
     *
     * @param request a {@link CustomerRequest} DTO with the updated data and the customer ID to update
     * @throws CustomerNotFoundException if the customer with the specified ID does not exist
     */
    public void updateCustomer(CustomerRequest request) {
        var customer = this.repository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("Cannot update customer:: No customer found with the provided ID: %s", request.id())
                ));
        mergeCustomer(customer, request);
        this.repository.save(customer);
    }

    /**
     * Merges updated fields from the request into the existing customer entity.
     * Updates only the fields provided (non-blank/nonnull).
     *
     * @param customer the {@link Customer} entity to update
     * @param request  the {@link CustomerRequest} containing updated customer info
     */
    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if (StringUtils.isNotBlank(request.firstname())) {
            customer.setFirstname(request.firstname());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if (request.address() != null) {
            customer.setAddress(request.address());
        }
    }

    /**
     * Retrieves all customers from the system.
     *
     * @return a {@link List} of {@link CustomerResponse} DTOs for every customer
     */
    public List<CustomerResponse> findAllCustomers() {
        return  this.repository.findAll()
                .stream()
                .map(this.mapper::fromCustomer)
                .collect(Collectors.toList());
    }

    /**
     * Finds a customer by their unique ID.
     *
     * @param id the unique ID of the customer
     * @return a {@link CustomerResponse} DTO for the matched customer
     * @throws CustomerNotFoundException if no customer is found with the specified ID
     */
    public CustomerResponse findById(String id) {
        return this.repository.findById(id)
                .map(mapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("No customer found with the provided ID: %s", id)));
    }

    /**
     * Checks whether a customer exists in the system by ID.
     *
     * @param id the unique ID of the customer
     * @return {@code true} if a customer exists with the given ID, {@code false} otherwise
     */
    public boolean existsById(String id) {
        return this.repository.findById(id)
                .isPresent();
    }

    /**
     * Deletes a customer by their unique ID.
     *
     * @param id the unique ID of the customer to delete
     */
    public void deleteCustomer(String id) {
        this.repository.deleteById(id);
    }

}
