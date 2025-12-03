package com.micro.payment.notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

/**
 * NotificationProducer
 * --------------------
 * Kafka-based event publisher for sending payment notification events to other microservices via messaging.
 *
 * <p>
 * <b>Purpose:</b>
 * <ul>
 *   <li>Decouples the payment process from notification delivery through asynchronous messaging.</li>
 *   <li>After a payment is processed, a notification event can be delivered to a notification service,
 *       email service, or other interested microservices via Kafka topic.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>How it works:</b>
 * <ul>
 *   <li>Uses Spring's KafkaTemplate, which handles serialization, connection, and error handling for sending messages.</li>
 *   <li>Each notification event (PaymentNotificationRequest) is published to the "payment-topic" Kafka topic.</li>
 *   <li>Annotation <code>@Service</code> makes the producer injectable in other beans (ie. PaymentService).</li>
 *   <li><code>@Slf4j</code> provides structured logging for debug and monitoring.</li>
 *   <li><code>@RequiredArgsConstructor</code> creates a constructor for required dependencies.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>System Flow Example:</b>
 * <ol>
 *   <li>PaymentService completes a payment.</li>
 *   <li>Calls sendNotification() to publish a PaymentNotificationRequest event.</li>
 *   <li>Other microservices or listeners subscribed to "payment-topic" receive and process the notification event.</li>
 * </ol>
 * </p>
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    /**
     * KafkaTemplate provided by Spring, parameterized with:
     * - key type (String): for partitioning, usually null or generic in notifications.
     * - value type (PaymentNotificationRequest): the event payload.
     */
    private final KafkaTemplate<String, PaymentNotificationRequest> kafkaTemplate;

    /**
     * Publishes a payment notification event to the "payment-topic" Kafka topic.
     *
     * <p>
     * <b>Steps:</b>
     * <ol>
     *  <li>Logs the notification publish intent and body for traceability.</li>
     *  <li>Builds a Message object with the notification request and topic header.</li>
     *  <li>Sends the message using KafkaTemplate; serialization to JSON is handled automatically by Spring Kafka.</li>
     * </ol>
     * </p>
     *
     * @param request The payment notification data (order reference, amount, customer details, etc.)
     */
    public void sendNotification(PaymentNotificationRequest request) {
        log.info("Sending notification with body = < {} >", request);

        // Build a Kafka message with payload and specify target topic
        Message<PaymentNotificationRequest> message = MessageBuilder
                .withPayload(request)          // Set message data as event object
                .setHeader(TOPIC, "payment-topic") // Specify Kafka topic for event
                .build();

        // Send message to Kafka (asynchronously by default)
        kafkaTemplate.send(message);
    }
}
