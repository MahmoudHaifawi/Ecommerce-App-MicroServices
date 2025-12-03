package com.micro.order.product;


import com.micro.order.excpetis.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * ProductClient
 * -------------
 * This Spring service enables communication from the order service to the product service.
 *
 * Why do we need it?
 * - In microservices, product-related data/actions (such as purchasing) are managed by the product service.
 * - The order service must request product purchases after customer/order validation.
 * - This class wraps the HTTP POST logic for the /purchase endpoint and provides typed results and error handling.
 *
 * How does it work?
 * - The product service URL is injected via application properties (`productUrl`), so it's deployable to many environments.
 * - When an order is being processed, the order service calls `purchaseProducts()`, passing a list of purchase requests.
 * - This method builds the proper HTTP request, sends it via RestTemplate, and receives a list of purchase results.
 * - If any problem occurs (e.g., service error, bad status), a BusinessException is thrown.
 *
 * Main Method:
 * - List<PurchaseResponse> purchaseProducts(List<PurchaseRequest> requestBody)
 *      - Sends requests to buy products, handles communication, returns responses.
 */
@Service
@RequiredArgsConstructor
public class ProductClient {

    @Value("${application.config.product-url}")
    private String productUrl; // Configured product service base URL

    private final RestTemplate restTemplate; // Used to send REST requests
    private final HttpServletRequest httpServletRequest; // ⬅️ inject current HTTP request
    /**
     * Attempts to purchase products by sending a POST request to the product service /purchase endpoint.
     *
     * @param requestBody List of PurchaseRequest objects describing what to buy
     * @return List of PurchaseResponse results from product service if successful
     * @throws BusinessException if product service returns an error status
     *
     * Process:
     * - Prepare HTTP headers for a JSON payload.
     * - Build HttpEntity with payload and headers.
     * - Define response type (list of PurchaseResponse) for automatic parsing.
     * - Send request to product service, receive and return parsed response.
     * - If status is error, throw an exception with details.
     */
    public List<PurchaseResponse> purchaseProducts(List<PurchaseRequest> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        /**
         * You can pass tokens (JWT) or API keys as HTTP headers in FeignClient or RestTemplate requests.
         * These prove your service’s identity, and the called service validates the token/key for secure integration—just like web clients,
         * but service-to-service!
         * This is a common and best practice in cloud-native and secure microservices environments.
         * */
        // ⬅️ forward Authorization header to product service (gateway)
        String authHeader = httpServletRequest.getHeader(AUTHORIZATION);
        if (authHeader != null && !authHeader.isBlank()) {
            headers.set(AUTHORIZATION, authHeader);
        }
        HttpEntity<List<PurchaseRequest>> requestEntity = new HttpEntity<>(requestBody, headers);
        ParameterizedTypeReference<List<PurchaseResponse>> responseType = new ParameterizedTypeReference<>() {}; // for coverting the respnse to the need response type

        ResponseEntity<List<PurchaseResponse>> responseEntity = restTemplate.exchange(
                productUrl + "/purchase",
                POST,
                requestEntity,
                responseType
        );

        // Error handling for failed requests to the product service
        if (responseEntity.getStatusCode().isError()) {
            throw new BusinessException("An error occurred while processing the products purchase: "
                    + responseEntity.getStatusCode());
        }

        return responseEntity.getBody();
    }
}
