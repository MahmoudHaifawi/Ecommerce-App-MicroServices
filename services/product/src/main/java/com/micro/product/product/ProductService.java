package com.micro.product.product;
import com.micro.product.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service // Marks this class as a Spring-managed service Bean
@RequiredArgsConstructor // Lombok: generates a constructor for all final fields (dependency injection)
public class ProductService {

    private final ProductRepository repository; // Handles DB operations for Product entity
    private final ProductMapper mapper;         // Maps between DTOs (request/response) and entity

    // Method to create a new Product from a request DTO and returns its new ID
    public Integer createProduct(
            ProductRequest request // DTO holding product data from the client
    ) {
        var product = mapper.toProduct(request); // Convert request DTO to Product entity
        return repository.save(product).getId(); // Save product to DB and return generated ID
    }

    // Find a product by its ID and convert to response DTO, or throw if not found
    public ProductResponse findById(Integer id) {
        return repository.findById(id)  // Look up Product entity by ID
                .map(mapper::toProductResponse) // Convert entity to response DTO if present
                .orElseThrow(() ->
                        new EntityNotFoundException("Product not found with ID:: " + id)); // Throw if not found
    }

    // Retrieve all products from DB and map to response DTOs
    public List<ProductResponse> findAll() {
        return repository.findAll()  // Get all products from DB as entities
                .stream()
                .map(mapper::toProductResponse) // Convert each to response DTO
                .collect(Collectors.toList());  // Collect into a List
    }

    // Atomically purchase multiple products, updating quantities and building responses
    @Transactional(rollbackFor = ProductPurchaseException.class) // DB ops in this method are transactional
    public List<ProductPurchaseResponse> purchaseProducts(
            List<ProductPurchaseRequest> request // List of product purchase requests (product ID/quantity)
    ) {
        var productIds = request
                .stream()
                .map(ProductPurchaseRequest::productId) // Extract product IDs from requests
                .toList();

        var storedProducts = repository.findAllByIdInOrderById(productIds); // Lookup all products by IDs

        // Validate: If some requested IDs do not exist, throw a custom exception
        if (productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One or more products does not exist");
        }

        // Sort purchase requests by product ID for predictable order
        var sortedRequest = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>(); // Collect responses

        // Loop over products and purchase requests in matching order
        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = sortedRequest.get(i);

            // Validate: If requested quantity exceeds available, throw custom exception
            if (product.getAvailableQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseException(
                        "Insufficient stock quantity for product with ID:: " + productRequest.productId());
            }

            // Update product's available quantity
            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);

            repository.save(product); // Persist updates to DB

            // Add purchase response DTO (includes updated info and purchased qty)
            purchasedProducts.add(mapper.toproductPurchaseResponse(product, productRequest.quantity()));
        }
        return purchasedProducts; // Return all purchase responses
    }
}