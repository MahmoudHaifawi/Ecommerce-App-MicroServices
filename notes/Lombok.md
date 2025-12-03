**Lombok** is a Java library that automatically generates common code (like getters, setters, constructors, `toString`, `equals`, etc.) using annotations, saving you from writing a lot of repetitive, boilerplate code.

***

### **Why Use Lombok?**

- **Reduces Boilerplate:** No need to manually write code for getters, setters, constructors, or builders.
- **Cleaner Classes:** Focus only on business logic and main fields.
- **Consistency:** Auto-generated methods are less error-prone and always correct.
- **Modern Practices:** Supports best practices like immutability & builder pattern easily.

***

### **When Should You Use Lombok?**

- **POJOs/Data Classes** – E.g., DTOs, entities, request/response models.
- **Builder pattern** – Use `@Builder` for complex object creation.
- **Immutability** – Use `@Value` for immutable objects.
- **Logging** – Use `@Slf4j` or similar logging annotations.
- **Equals & HashCode** – Auto-generate these without manual mistakes.
- **Java projects with lots of simple models** – Any place where you see repeated code across different classes.

***

### **Common Lombok Annotations**

| Annotation     | Purpose                               | Example Use                                |
|----------------|---------------------------------------|--------------------------------------------|
| `@Getter`      | Auto-generates getters for all fields | DTOs, entities                            |
| `@Setter`      | Auto-generates setters                | DTOs, config beans                        |
| `@ToString`    | Generates `toString()`                | Debugging                                 |
| `@EqualsAndHashCode` | Generates `equals`, `hashCode`  | Entities, value objects                   |
| `@NoArgsConstructor` | Generates no-args constructor    | JPA entities                              |
| `@AllArgsConstructor`| Generates all-args constructor   | DTOs, test data                           |
| `@Builder`     | Adds builder pattern                  | Complex object creation                   |
| `@Data`        | Combines getter, setter, equals, hashCode, toString | Data classes         |
| `@Value`       | Immutable data class (all fields final) | Value objects, responses    |
| `@Slf4j`       | Auto-create slf4j logger              | For logging inside classes                |

***

### **How to Use Lombok (Example)**

```java
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class User {
    private String id;
    private String name;
    private int age;
}
```
- Now you get **getters, setters, `toString`, `equals`, hashCode**, and can build users like:
```java
User u = User.builder().id("1").name("Alice").age(25).build();
```

***

### **When Not to Use Lombok**

- If your team/project forbids third-party compile-time tools.
- If you need custom method logic in your accessors/mutators.
- Rare compatibility issues with some tools/IDEs (mostly solved today).

***

**Summary:**  
Use **Lombok** to make your Java code simpler, cleaner, and more maintainable—especially for model classes in Spring projects and microservices. It's safe, popular, and makes modern Java development much more pleasant!

[1](https://www.youtube.com/watch?v=jdeSV0GRvwI&list=PL41m5U3u3wwm-27nQk1rUqJRIRtnUoqMj&index=12)