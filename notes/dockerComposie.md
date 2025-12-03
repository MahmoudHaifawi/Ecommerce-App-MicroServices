Here it is in clean, structured **Markdown**:

---

# **Service-by-Service Explanation**

## **PostgreSQL**

* Runs a PostgreSQL database in a container.
* Sets up database name and password.
* Persists data using a Docker volume.
* Exposes port **5432** for database connections.

---

## **pgAdmin**

* Provides a web UI for managing PostgreSQL.
* Credentials are set via environment variables (with defaults).
* Persists its own configuration using a Docker volume.
* Exposes port **5050** (mapped to **80** inside the container).

---

## **Zipkin**

* Distributed tracing system for microservices.
* Exposes port **9411**.

---

## **MongoDB**

* Starts a MongoDB database container.
* Persists database data using a volume.
* Sets root username and password.
* Exposes port **27017**.

---

## **Mongo Express**

* Web UI for MongoDB management.
* Connects to MongoDB using credentials.
* Exposes port **8081**.

---

## **ZooKeeper**

* Service registry required for Kafka.
* Runs ZooKeeper on port **2181** (mapped to **22181** externally).

---

## **Kafka**

* Message broker for asynchronous communication.
* Depends on ZooKeeper.
* Exposes port **9092**.
* Configures several Kafka environment settings for local development.

---

## **MailDev**

* Lightweight fake SMTP server for development.
* Exposes:

  * **1080** (web UI)
  * **1025** (SMTP server)

---

## **Networks & Volumes**

* All services run on the `microservices-net` Docker network.
* Persistent volumes created for:

  * **postgres**
  * **pgadmin**
  * **mongo**

---

```mermaid
graph LR
  %% External clients
  DevBrowser[Developer Browser]:::client
  AppSMTP[App / Service SMTP Client]:::client

  %% Network
  subgraph NET[microservices-net Docker network]
    subgraph DB[Databases]
      Postgres[(PostgreSQL<br/>5432)]:::db
      Mongo[(MongoDB<br/>27017)]:::db
    end

    subgraph DB_UI[DB Web UIs]
      PgAdmin[pgAdmin<br/>5050 -> 80]:::tool
      MongoExpress[mongo-express<br/>8081]:::tool
    end

    subgraph Messaging[Messaging & Tracing]
      ZooKeeper[ZooKeeper<br/>22181 -> 2181]:::infra
      Kafka[Kafka<br/>9092]:::infra
      Zipkin[Zipkin<br/>9411]:::tool
    end

    subgraph Mail[Mail / SMTP]
      MailDev[MailDev<br/>Web:1080 / SMTP:1025]:::tool
    end

    subgraph Apps[Your Microservices / Apps]
      App1[Service A]:::app
      App2[Service B]:::app
    end
  end

  %% Relations: Dev tools
  DevBrowser -->|HTTP 5050| PgAdmin
  DevBrowser -->|HTTP 8081| MongoExpress
  DevBrowser -->|HTTP 9411| Zipkin
  DevBrowser -->|HTTP 1080| MailDev

  %% DB access from services
  App1 -->|JDBC 5432| Postgres
  App2 -->|JDBC 5432| Postgres
  App1 -->|Mongo Driver 27017| Mongo
  App2 -->|Mongo Driver 27017| Mongo

  %% Kafka & ZooKeeper
  Kafka <-->|Coordination| ZooKeeper
  App1 -->|Produce/Consume 9092| Kafka
  App2 -->|Produce/Consume 9092| Kafka

  %% Tracing
  App1 -->|Trace Spans| Zipkin
  App2 -->|Trace Spans| Zipkin

  %% MailDev usage
  AppSMTP -->|SMTP 1025| MailDev

  classDef app fill:#e3f2fd,stroke:#1565c0,stroke-width:1px;
  classDef db fill:#e8f5e9,stroke:#2e7d32,stroke-width:1px;
  classDef infra fill:#fff3e0,stroke:#ef6c00,stroke-width:1px;
  classDef tool fill:#f3e5f5,stroke:#6a1b9a,stroke-width:1px;
  classDef client fill:#eeeeee,stroke:#424242,stroke-width:1px;


