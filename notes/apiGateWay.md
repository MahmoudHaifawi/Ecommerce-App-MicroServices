**Summary of API Gateway / Backends for Frontends Pattern**:[1]

- **Context:**  
  In a microservices architecture, different clients (web, mobile, third parties) require different data or APIs and often need to aggregate information from multiple services. For example, an online store's product details page gathers data from various microservices.

- **Problem:**  
  Clients must call several microservices to gather necessary information, leading to high complexity and inefficiency, especially for mobile or low-bandwidth clients.

- **Solution:**  
  Introduce an **API Gateway** as a single entry point for all client requests. The gateway can:
    - Proxy requests to underlying services
    - Aggregate/fan-out requests to multiple services
    - Provide different APIs tailored for each client type

- **Backends for Frontends (BFF) Variation:**  
  Each client type (web, mobile, third-party) gets its own gateway, allowing APIs and aggregations tailored to its needs.

- **Benefits:**
    - Hides service partitioning and instance locations from clients
    - Delivers optimized APIs per client
    - Reduces client-server round-trips
    - Centralizes cross-cutting concerns (security, protocol translation)
    - Simplifies client logic

- **Drawbacks:**
    - Adds complexity: one more component to develop, deploy, maintain
    - Possibility of increased latency (extra network hop)

- **Implementation Considerations:**
    - Use reactive/event-driven technologies (Netty, Spring Reactor, NodeJS) for scalability
    - Ensure high availability (multiple instances, load balancers)

- **Related Patterns:**
    - Client-side/server-side discovery
    - Access token propagation
    - Circuit breaker
    - API composition

- **Known Uses & Examples:**
    - Netflix API Gateway
    - Java/Spring API Gateway demo projects

This pattern is essential to make microservices architectures maintainable, scalable, and usable by diverse clients.

[1](https://microservices.io/patterns/apigateway.html)