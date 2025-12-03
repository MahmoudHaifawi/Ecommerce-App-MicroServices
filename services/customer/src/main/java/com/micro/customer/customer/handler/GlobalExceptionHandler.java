package com.micro.customer.customer.handler;


import com.micro.customer.customer.exception.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Handles exceptions globally across all REST controllers in the application.
 * <p>
 * This class is annotated with {@link org.springframework.web.bind.annotation.RestControllerAdvice},
 * which allows it to intercept and process exceptions thrown by any controller annotated with
 * {@code @RestController} within the application. This promotes centralized error handling, improves code
 * maintainability, and ensures consistent error responses to clients.
 * </p>
 *
 * <p>
 * <b>When is it used?</b>
 * <ul>
 *   <li>It is automatically invoked whenever a controller method throws an exception
 *       that matches one of the {@code @ExceptionHandler} methods defined here.</li>
 *   <li>It is especially useful for handling custom exceptions (like {@code CustomerNotFoundException}) and
 *       validation errors (such as {@code MethodArgumentNotValidException} thrown during request body validation).</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Why do we create this?</b>
 * <ul>
 *   <li>To avoid repetitive exception handling code across multiple controllers.</li>
 *   <li>To return meaningful and uniform error messages or status codes to API clients.</li>
 *   <li>To log errors in a single place or perform cross-cutting concerns if needed.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Annotations:</b>
 * <ul>
 *   <li>{@code @RestControllerAdvice} – A specialization of {@code @ControllerAdvice} used for REST APIs.
 *       It allows for global exception handling and returns response bodies by default.</li>
 *   <li>{@code @ExceptionHandler} – Marks methods that handle specified exception types. When the specified
 *       exception is thrown anywhere in a controller, the corresponding method is executed.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Exception Handling Methods:</b>
 * <ul>
 *   <li><b>handle(CustomerNotFoundException exp):</b>
 *     <ul>
 *       <li>Handles cases when a customer is not found.</li>
 *       <li>Returns a {@code 404 NOT FOUND} response with the exception message as the body.</li>
 *     </ul>
 *   </li>
 *   <li><b>handleMethodArgumentNotValidException(MethodArgumentNotValidException exp):</b>
 *     <ul>
 *       <li>Handles validation errors on request bodies.</li>
 *       <li>Builds a map of field errors and returns it inside an {@code ErrorResponse} object, with a
 *           {@code 400 BAD REQUEST} status code.</li>
 *     </ul>
 *   </li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Summary:</b>
 * <br>
 * The {@code GlobalExceptionHandler} enhances the robustness and user experience of the API by
 * providing a consistent mechanism for error reporting and handling across all endpoints.
 * </p>
 */


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handle(CustomerNotFoundException exp) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exp.getMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        var errors = new HashMap<String, String>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var fieldName = ((FieldError) error).getField();
                    var errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponse(errors));
    }
}