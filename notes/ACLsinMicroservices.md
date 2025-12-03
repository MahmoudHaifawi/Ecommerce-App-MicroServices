
---

# **ACLs in Microservices (Access Control Lists)**

In microservices, **ACLs (Access Control Lists)** determine **who** (or **what service**) can access a specific resource or API.
They apply to **users** and **service-to-service communication**.

---

## **How Are ACLs Enforced Between Microservices?**

### **1. Service Identity**

Each microservice has its own identity such as:

* API key
* Service account
* mTLS certificate
* OAuth2/OIDC client credentials

When `Service A` calls `Service B`, it includes proof of its identity.
**Example:** A JWT signed by your authorization server.

---

### **2. API Gateway / Service Mesh**

Service meshes (Istio, Linkerd) and API gateways (Kong, NGINX, AWS API Gateway):

* Authenticate the incoming service
* Check ACLs before forwarding the request
* Enforce rate limits, quotas, RBAC/ABAC rules

This centralizes **authentication** and **authorization** for your whole system.

---

### **3. Token-Based Security (JWT, OAuth2)**

A microservice calling another will:

1. Request or generate a service token
2. Include it in the request header
3. The receiving service validates:

    * Token signature
    * Token expiration
    * Service role/permissions (ACL rules)

**Example:**
OrderService sends a JWT with a claim:
`"service": "order-service"`
Then ProductService checks its ACL to see if this service is allowed to perform the action.

---

### **4. Application-Level ACL Checks**

A service can also maintain its own **internal ACL list**, such as:

* Which services can call which endpoint
* What roles/services can perform an operation
* Which actions are restricted

Example:
`CustomerService` only allows `OrderService` to call `getCustomerBalance()`.

---

### **5. Kubernetes RBAC + Network Policies**

If deployed on Kubernetes:

* **RBAC** restricts what each service account can do
* **NetworkPolicies** restrict which pods can talk to which other pods

This prevents unauthorized lateral movement inside the cluster.

---

# **Example Flow (Order → Customer)**

`OrderService` needs to call `CustomerService`:

1. OrderService gets/generates a JWT or service token
2. Adds token to the HTTP request header
3. CustomerService validates the token
4. ACL check:

    * "Is OrderService authorized to call this endpoint?"
5. If allowed → request proceeds
6. If not → return 403 Forbidden

---

# **Why ACLs Matter in Microservices**

### **Security**

Prevents unauthorized access between services.

### **Compliance**

Ensures only allowed services can perform sensitive operations
(e.g., only PaymentService can charge cards).

### **Auditing**

All inter-service access can be tracked.

---

# **Summary**

ACLs for service-to-service communication are enforced through:

* **Service identities**
* **Token-based security (JWT/OAuth2)**
* **API Gateways / Service Meshes**
* **Application-level ACL checks**
* **Kubernetes RBAC and network policies**

Every microservice verifies if the **caller** is allowed to access a resource before granting access — even if the caller is another microservice.

---


