package com.micro.order.order;
import com.micro.order.orderLine.OrderLine;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@AllArgsConstructor // Lombok: generates a constructor with all fields
@Builder // Lombok: enables builder pattern for object creation
@Getter // Lombok: auto-generates getters for all fields
@Setter // Lombok: auto-generates setters for all fields
@Entity // JPA: designates this class as a table-mapped entity
@EntityListeners(AuditingEntityListener.class) // JPA: enables auditing (timestamps)
@NoArgsConstructor // Lombok: generates a no-arg constructor
@Table(name = "customer_order") // JPA: maps this entity to "customer_order" table
public class Order {

    @Id // JPA: primary key indicator
    @GeneratedValue // JPA: auto-generates unique id
    private Integer id; // Unique ID for the order

    @Column(unique = true, nullable = false) // JPA: unique and required column
    private String reference; // Order reference string (e.g. order number)

    private BigDecimal totalAmount; // The total money amount for the order

    @Enumerated(EnumType.STRING) // JPA: stores enum as Strings in DB
    private PaymentMethod paymentMethod; // Payment method type (enum)

    private String customerId; // ID of customer who placed the order

    @OneToMany(mappedBy = "order") // JPA: one order has many order lines; "order" is the field in OrderLine
    private List<OrderLine> orderLines; // List of order lines (products in this order)

    @CreatedDate // Spring Data: creation timestamp (auto-managed)
    @Column(updatable = false, nullable = false) // JPA: cannot update, must always be present
    private LocalDateTime createdDate; // Timestamp for when the order was created

    @LastModifiedDate // Spring Data: last modify timestamp (auto-managed)
    @Column(insertable = false) // JPA: not set on creation, only after updating
    private LocalDateTime lastModifiedDate; // Last time order was modified
}