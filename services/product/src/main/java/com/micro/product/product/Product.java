package com.micro.product.product;

import com.micro.product.category.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Lombok annotation to generate a constructor with all arguments
@AllArgsConstructor
// Lombok annotation to generate a no-argument constructor
@NoArgsConstructor
// Lombok annotation to enable builder pattern for Product
@Builder
// Lombok annotation to generate getter methods for all fields
@Getter
// Lombok annotation to generate setter methods for all fields
@Setter
// JPA annotation to mark the class as an entity (table in database)
@Entity
public class Product {
    // JPA annotation to mark 'id' as the primary key
    @Id
    // JPA annotation to generate value automatically for 'id'
    @GeneratedValue
    private Integer id; // Unique identifier for each product instance

    private String name; // Name of the product

    private String description; // Description of the product

    private double availableQuantity; // Number of items available in stock

    private BigDecimal price; // Price of the product using BigDecimal for precision

    // JPA annotation for many-to-one relationship with Category
    @ManyToOne
    // Specifies the foreign key column name in the product table
    @JoinColumn(name = "category_id")
    private Category category; // Reference to the category this product belongs to
}
