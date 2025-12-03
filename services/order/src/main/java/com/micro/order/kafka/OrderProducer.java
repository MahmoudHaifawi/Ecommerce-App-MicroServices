package com.micro.order.kafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

/**
 * OrderProducer
 * -------------
 * This service is responsible for publishing order confirmation events to a Kafka topic.
 *
 * <p>
 * Why do we need it?
 * - In a microservices system using event-driven architecture, we often want to notify other services when important business events occur.
 * - For example, when a new order is placed, we send an order confirmation event so that other services (delivery, notifications, analytics) can react.
 * </p>
 *
 * <p>
 * How does it work?
 * - Uses Spring Kafka's KafkaTemplate to send messages to a Kafka topic.
 * - Uses Spring Messaging's MessageBuilder to create messages with headers (including the Kafka topic).
 * - Annotated as a Spring Service so it can be auto-wired and reused.
 * </p>
 *
 * <p>
 * Main method: sendOrderConfirmation(OrderConfirmation)
 * - Builds a message containing order confirmation details.
 * - Sets the Kafka topic header.
 * - Sends the message via KafkaTemplate.
 * - Logs the sending action for monitoring and debugging.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {

    /**
     * KafkaTemplate is provided by Spring Kafka.
     * It's a helper that simplifies sending messages to Kafka topics.
     * - Key: String (can be used for partitioning)
     * - Value: OrderConfirmation (the actual event payload)
     */
    private final KafkaTemplate<String, OrderConfirmation> kafkaTemplate;

    /**
     * Publishes an OrderConfirmation event to the "order-topic" Kafka topic.
     *
     * @param orderConfirmation the confirmation details to be sent
     * <p>
     * Process:
     * 1. Log the intent to send an event.
     * 2. Build a message with the payload and topic header using MessageBuilder.
     * 3. Use kafkaTemplate to send the message (handles serialization).
     * </p>
     */
    public void sendOrderConfirmation(OrderConfirmation orderConfirmation) {
        log.info("Sending order confirmation event to Kafka");

        // 1. Build a message object with payload and topic header ("order-topic")
        Message<OrderConfirmation> message = MessageBuilder
                .withPayload(orderConfirmation)           // Attach the event payload
                .setHeader(TOPIC, "order-topic")          // Specify Kafka topic using header
                .build();                                 // Build message

        // 2. Send the constructed message to Kafka
        // KafkaTemplate will serialize the OrderConfirmation object (e.g., to JSON)
        kafkaTemplate.send(message);
    }
}
