
***

### What is the Saga Pattern?
The **Saga pattern** solves the problem of maintaining data consistency across multiple microservices when a single business process spans services and databases. In a standard transaction, everything happens in one database, but in microservices, transactions involve different databases—so distributed (ACID) transactions are not practical.

Instead, the Saga pattern breaks up a big business process into smaller, independent steps (local transactions) handled by each service. If a step fails, the pattern triggers compensating (rollback/undo) actions to keep systems in sync.

***

#### Step-by-Step Explanation

1. **Start (Order Service)**
    - The user places an order.
    - Order Service creates the order and sends an event (“Order Placed”) to Inventory Service.

2. **Inventory Service**
    - Receives the “Order Placed” event.
    - Tries to reserve items.
    - If successful, sends “Inventory Reserved” event to Payment Service.
    - If reservation fails, sends “Order Failed” back to Order Service to cancel the order.

3. **Payment Service**
    - Receives “Inventory Reserved” event.
    - Tries to process payment.
    - If payment succeeds, the order is completed.
    - If payment fails, sends failure event—Inventory Service undoes reservation (“Inventory Released”) and Order Service cancels the order.

4. **Compensation**
    - If anything fails in the chain, compensation events are triggered:
        - Undo inventory reservation.
        - Mark the order as cancelled.

***

### Visual Flowchart
See the diagram below for a visual explanation of how Saga pattern flows through services, including forwards steps and compensation on failure:

***

### Key Points
- **Saga = distributed transaction broken into steps.**
- **Each step is a local transaction in a service.**
- **Failure triggers rollback (“compensation”) steps.**
- **No global locking! Services communicate via events/messages.**
- **Choreography (event-driven) or orchestration (central coordinator) modes.**

Let me know if you want a real snippet in Java/Spring Boot, or more details on another type of saga pattern!

[1](https://www.youtube.com/watch?v=jdeSV0GRvwI&list=PL41m5U3u3wwm-27nQk1rUqJRIRtnUoqMj&index=9)

```mermaid
sequenceDiagram
    participant User
    participant OrderService
    participant InventoryService
    participant PaymentService

    User->>OrderService: Place Order
    OrderService->>InventoryService: OrderPlaced
    InventoryService->>PaymentService: InventoryReserved
    PaymentService->>OrderService: PaymentSuccessful

    alt Inventory Failure
        InventoryService-->>OrderService: OrderFailed
        OrderService-->>User: Order Cancelled
    end

    alt Payment Failure
        PaymentService-->>InventoryService: PaymentFailed
        InventoryService-->>OrderService: InventoryReleased
        OrderService-->>User: Order Cancelled
    end
