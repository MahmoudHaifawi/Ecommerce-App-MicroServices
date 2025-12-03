## What is RestTemplate?

**RestTemplate** is a Spring class for making synchronous HTTP requests from a Java application.
You use it to call external REST APIs from your Spring service, handle results as Java objects, send GET/POST/PUT/DELETE/PATCH requests, and work with responses directly.

---

### Why choose RestTemplate?

- **Simplicity:** Easy to use for typical REST calls.
- **Control:** Lets you customize URLs, headers, error handling.
- **Good for legacy Spring apps:** Works in Spring and Spring Boot; often present in older codebases.
- **Testing:** Handy for integration tests or quick HTTP calls.

---

### When and How to Use

**When**
- When you need to call an external REST API.
- When you want to customize request/response handling.
- In apps not using Spring Cloud (or when Feign is overkill).
- For legacy and testing needs.

**How**
1. **Dependency (usually already there with Spring Boot):**
    ```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    ```

2. **Bean configuration:**
    ```
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    ```

3. **Making requests:**
    ```
    // Inject RestTemplate
    @Autowired
    private RestTemplate restTemplate;

    // GET example
    String url = "https://api.example.com/data";
    ResponseEntity<MyResponseDto> response =
        restTemplate.getForEntity(url, MyResponseDto.class);

    // POST example
    MyRequestDto req = new MyRequestDto(...);
    ResponseEntity<MyResponseDto> response =
        restTemplate.postForEntity(url, req, MyResponseDto.class);
    ```

---

### Pros and Cons Table

| Use Case          | Pros                                   | Cons                         |
|-------------------|----------------------------------------|------------------------------|
| General API calls | Simple, flexible, direct               | Blocking, not declarative    |
| Legacy/Testing    | Familiar, compatible                   | More code to maintain        |
| Microservices     | Full control over HTTP requests/headers | Not auto-integrated with service discovery |

---

### Summary

- RestTemplate = quick, flexible HTTP client for Spring.
- Best for simple external calls, legacy code, or tests.
- For richer microservice integration (service discovery, load balancing, automatic serialization), prefer **Feign Client** or **WebClient** (Spring’s newer reactive HTTP client).
