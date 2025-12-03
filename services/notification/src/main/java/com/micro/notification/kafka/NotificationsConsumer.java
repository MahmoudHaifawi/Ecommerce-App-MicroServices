package com.micro.notification.kafka;
import com.micro.notification.email.EmailService;
import com.micro.notification.kafka.order.OrderConfirmation;
import com.micro.notification.kafka.payment.PaymentConfirmation;
import com.micro.notification.notification.Notification;
import com.micro.notification.notification.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import static com.micro.notification.notification.NotificationType.ORDER_CONFIRMATION;
import static com.micro.notification.notification.NotificationType.PAYMENT_CONFIRMATION;
import static java.lang.String.format;

/**
 * NotificationsConsumer
 *
 * This Spring Service listens for Kafka events (payment, order) and handles user notifications.
 * It persists notification events to the database and sends emails to customers.
 *
 * <p><b>Core Responsibilities:</b>
 * <ul>
 *   <li>Consumes messages from "payment-topic" and "order-topic" Kafka topics.</li>
 *   <li>Packs each event (PaymentConfirmation/OrderConfirmation) into a Notification entity and saves it.</li>
 *   <li>Sends appropriate notification emails to customers on each event.</li>
 * </ul>
 *
 * <b>Dependencies Injected via Constructor:</b>
 * <ul>
 *   <li><b>NotificationRepository:</b> JPA repository for persisting notification records.</li>
 *   <li><b>EmailService:</b> Service for sending notification emails (payment and order).</li>
 * </ul>
 *
 * <b>Annotations Used:</b>
 * <ul>
 *   <li><b>@Service:</b> Registers as a Spring bean for injection.</li>
 *   <li><b>@Slf4j:</b> Adds SLF4J logger 'log' for logging.</li>
 *   <li><b>@RequiredArgsConstructor:</b> Lombok: generates constructor for final fields.</li>
 *   <li><b>@KafkaListener:</b> Marks methods as Kafka consumers for specific topics.</li>
 * </ul>
 *
 * <b>Exception Handling:</b>
 * <ul>
 *   <li>Methods throw MessagingException if email sending fails.</li>
 * </ul>
 */
// Marks this class as a Spring-managed service component
@Service
// Lombok annotation: adds a logger (log) instance for this class
@Slf4j
// Lombok: generates constructor for final fields (dependency injection)
@RequiredArgsConstructor
public class NotificationsConsumer {

    // JPA repository for saving notification entities to the DB
    private final NotificationRepository repository;
    // Service for sending notification emails
    private final EmailService emailService;

    /**
     * Consumes payment events from the Kafka "payment-topic".
     * Logs the event, saves notification to DB, and sends payment success email.
     *
     * @param paymentConfirmation The received payment event information.
     * @throws MessagingException If there is a failure in sending email notifications.
     */

    @KafkaListener(topics = "payment-topic") // Listens to payment confirmation events from the "payment-topic" Kafka topic
    public void consumePaymentSuccessNotifications(
            PaymentConfirmation paymentConfirmation
    ) throws MessagingException {
        // Log receipt of Kafka message for auditing/troubleshooting
        log.info(format("Consuming the message from payment-topic Topic:: %s", paymentConfirmation));
        // Persist notification event to database
        repository.save(
                Notification.builder()
                        .type(PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now()) // Timestamp event
                        .paymentConfirmation(paymentConfirmation) // Embed payment data in notification entity
                        .build()
        );
        // Prepare customer name for email
        var customerName = paymentConfirmation.customerFirstname() + " " + paymentConfirmation.customerLastname();
        // Send notification email for successful payment
        emailService.sendPaymentSuccessEmail(
                paymentConfirmation.customerEmail(),
                customerName,
                paymentConfirmation.amount(),
                paymentConfirmation.orderReference()
        );
    }
/**
        * Consumes order events from the Kafka "order-topic".
            * Logs the event, saves notification to DB, and sends order confirmation email.
            *
            * @param orderConfirmation The received order event information.
            * @throws MessagingException If there is a failure in sending email notifications.
            */
    @KafkaListener(topics = "order-topic")// Listens to order confirmation events from the "order-topic" Kafka topic
    public void consumeOrderConfirmationNotifications(
            OrderConfirmation orderConfirmation
    ) throws MessagingException {
        // Log receipt of Kafka message for auditing/troubleshooting
        log.info(format("Consuming the message from order-topic Topic:: %s", orderConfirmation));
        // Persist notification event to database
        repository.save(
                Notification.builder()
                        .type(ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now()) // Timestamp event
                        .orderConfirmation(orderConfirmation) // Embed order data in notification entity
                        .build()
        );
        // Prepare customer name for email
        var customerName = orderConfirmation.customer().firstname() + " " + orderConfirmation.customer().lastname();
        // Send notification email for successful order
        emailService.sendOrderConfirmationEmail(
                orderConfirmation.customer().email(),
                customerName,
                orderConfirmation.totalAmount(),
                orderConfirmation.orderReference(),
                orderConfirmation.products()
        );
    }
}

