package com.micro.order.orderLine;
import com.micro.order.order.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor // Lombok: all-fields constructor
@Builder // Lombok: builder pattern
@Getter // Lombok: getters for all fields
@Setter // Lombok: setters for all fields
@Entity // JPA: marks this as a DB-mapped entity
@NoArgsConstructor // Lombok: no-argument constructor
@Table(name = "customer_line") // JPA: table name mapping
public class OrderLine {

    @Id // JPA: primary key for order line
    @GeneratedValue // JPA: auto-generated unique ID
    private Integer id; // Unique ID for each order line

    @ManyToOne // JPA: many lines can belong to one order
    @JoinColumn(name = "order_id") // JPA: foreign key in this table pointing to order
    private Order order; // Reference to parent order

    private Integer productId; // Product ID in this line

    private double quantity; // Quantity of that product in the order line
}