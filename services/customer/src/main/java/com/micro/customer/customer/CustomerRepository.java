package com.micro.customer.customer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 * Repository interface for managing {@link Customer} entities in MongoDB.
 * <p>
 * Extends {@link org.springframework.data.mongodb.repository.MongoRepository} to inherit
 * standard CRUD operations and query methods for the Customer document.
 * <ul>
 *   <li>Provides built-in methods like save, findById, findAll, deleteById, etc., for working with customer data.</li>
 *   <li>Promotes separation of data-access logic from business logic, following the repository pattern.</li>
 *   <li>Integrates with Spring Data MongoDB for automatic implementation and dependency injection.</li>
 *   <li>The first generic parameter is the entity type (Customer), the second is the ID type (String).</li>
 * </ul>
 * Custom query methods can be added here if advanced database queries are needed.
 */
public interface CustomerRepository extends MongoRepository<Customer, String> {
}
