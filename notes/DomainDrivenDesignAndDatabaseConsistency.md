# Domain-Driven Design and Database Consistency in Microservices

## Key Concepts

- **Microservices use Domain-Driven Design (DDD):**
    - Each microservice represents a _bounded context_, owning its own domain model and database.
    - Database schemas and logic are optimized for each service’s domain.
    - There’s _no shared database_; every microservice manages its own data.

---

## Why Not Consistent Cross-Database Transactions?

- **Distributed transactions (ACID across multiple DBs) are avoided:**
    - Each service is isolated—with its own DB and tech stack.
    - There’s no way to have a “global transaction” that spans multiple microservice databases.
    - Distributed transaction protocols (e.g., 2-phase commit) introduce scaling and reliability challenges.

- **You cannot lock records across all DBs together**:
    - No support for multi-database transaction boundaries.
    - If one database fails, others continue operating—improving failure isolation.

---

## Architectural Reasons

- **Loose Coupling & Scalability:**
    - Independence for updates, maintenance, and deployment.
    - Services can be scaled independently.
- **Failure Isolation:**
    - Problems in one DB/service do not affect others.
- **Bounded Context:**
    - Data model and business logic are owned by their domain.

---

## How Is Data Consistency Achieved?

- **Eventual Consistency (not instant):**
    - Changes affecting multiple domains are split up, handled independently.
    - Each service performs its local transaction, then _publishes domain events_ (like “OrderCreated” or “PaymentSucceeded”).
- **Sagas & Compensating Transactions:**
    - Long-running, multi-step flows use the Saga pattern: a series of local transactions with compensating actions if needed.

  **Example Pattern:**
    - Order service creates order in its own DB → emits OrderPlaced event.
    - Payment service listens, processes payment in its own DB → emits PaymentCompleted event.
    - Shipping service listens, ships product in its own DB.
    - If any step fails, earlier steps are “undone” via compensating events.

---

## **Anti-Pattern: Shared Database**

- Using a single shared database for all microservices _violates DDD and microservices principles:_
    - Tight coupling, schema drift, lack of failure isolation, harder evolution of domains.

---

## Summary Table

| Feature          | Monolith (Shared DB)   | Microservices (DDD)       |
|------------------|-----------------------|---------------------------|
| Consistency      | Strong (ACID)         | Eventual (domain events)  |
| Data Model       | Single, app-wide      | Per service/domain        |
| Transaction      | All domains/tables    | Local per service         |
| Coupling         | Tight                 | Loose (bounded context)   |
| Failure isolation| Low                   | High                      |
| Scalability      | Whole app             | Per-service               |

---

## Diagram: Data Consistency in Microservices

![Data Consistency Architecture](https://ppl-ai-code-interpreter-files.s3.amazonaws.com/web/direct-files/9c1ce666df5528a3995a57de5cc292e7/813d48a0-21fc-470b-864d-75179677a24c/c567411d.png)

---

**In summary:**
- Each microservice manages its own data and maintains boundaries using domain-driven design.
- System-wide consistency is eventual, relying on events and Sagas—not instant global transactions.

