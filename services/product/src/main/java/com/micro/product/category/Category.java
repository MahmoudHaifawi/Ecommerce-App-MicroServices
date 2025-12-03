package com.micro.product.category;
import com.micro.product.product.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// Lombok annotation to generate a constructor with arguments for all fields
@AllArgsConstructor
// Lombok annotation to generate a no-argument constructor
@NoArgsConstructor
// Lombok annotation to enable builder pattern for the class
@Builder
// Lombok annotation to generate getter methods for all fields
@Getter
// Lombok annotation to generate setter methods for all fields
@Setter
// JPA annotation to mark this class as a persistent entity
@Entity
public class Category {

    // JPA annotation to mark 'id' as the primary key
    @Id
    // JPA annotation to auto-generate the value of 'id'
    @GeneratedValue
    private Integer id; // Unique identifier for each category

    private String name; // The name of the category

    private String description; // Description about the category

    // JPA annotation to define a one-to-many relationship with Product entity
    // 'mappedBy = "category"' means Product owns the relationship via its 'category' field
    // 'cascade = CascadeType.REMOVE' means deleting Category removes its Products
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Product> products; // List of products belonging to this category
}
