package com.micro.order.product;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

/**
 * In microservices, never import or share DTO/model classes across service boundaries.
 *
 * Always define your own response/request DTOs in your own service, and map/interact via JSON over HTTP.
 *
 * This keeps services loosely coupled, safe, and independently maintainable.
 * You only change your DTOs if your dependency on the remote API changes, not because the producer service changes its code or models.
 * */
@Validated
public record PurchaseRequest(
        @NotNull(message = "Product is mandatory")
        Integer productId,
        @Positive(message = "Quantity is mandatory")
        double quantity
) {
}

