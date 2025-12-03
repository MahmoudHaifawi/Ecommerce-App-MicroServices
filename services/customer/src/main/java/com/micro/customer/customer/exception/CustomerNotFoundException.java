package com.micro.customer.customer.exception;




import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Exception thrown when a requested customer cannot be found in the system.
 * <p>
 * This exception is typically used in service or repository layers when attempting
 * to retrieve a customer by ID, email, or other attributes, and no matching customer exists.
 * It extends {@link RuntimeException}, making it an unchecked exception.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 *     if (customer == null) {
 *         throw new CustomerNotFoundException("Customer with ID " + id + " not found");
 *     }
 * </pre>
 </p>
 * Lombok annotations used in this class:
 *
 * <ul>
 *   <li>
 *     <b>@Data</b>: Generates boilerplate code such as getters, setters, {@code toString()},
 *     {@code equals()}, and {@code hashCode()} methods. This improves code maintainability
 *     and reduces repetitive coding, especially useful when fields may be added later.
 *   </li>
 *   <li>
 *     <b>@EqualsAndHashCode(callSuper = true)</b>: Ensures that {@code equals()} and
 *     {@code hashCode()} methods include fields from both this class and its superclass
 *     ({@link RuntimeException}). This is important for correct comparisons and usage
 *     in collections when custom fields are added to the exception.
 *   </li>
 * </ul>
 * The use of these annotations streamlines the class definition, making exception handling
 * cleaner and more robust.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerNotFoundException extends RuntimeException {

    private final String msg;
}
