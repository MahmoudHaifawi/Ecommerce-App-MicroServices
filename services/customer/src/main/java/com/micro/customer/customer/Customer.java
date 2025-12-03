package com.micro.customer.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Represents a customer entity in the e-commerce system.
 * <p>
 * This class is mapped to a MongoDB collection through the {@code @Document} annotation.
 * It utilizes Lombok annotations to reduce boilerplate code:
 * <ul>
 *   <li>{@code @AllArgsConstructor} - Generates a constructor with all fields.</li>
 *   <li>{@code @NoArgsConstructor} - Generates a default constructor with no arguments.</li>
 *   <li>{@code @Builder} - Enables the builder pattern for flexible object creation.</li>
 *   <li>{@code @Getter} and {@code @Setter} - Automatically generate getter and setter methods for all fields.</li>
 * </ul>
 * The class contains basic identity and contact fields, such as customer ID, name, email, and address.
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Customer {

    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private Address address;
}