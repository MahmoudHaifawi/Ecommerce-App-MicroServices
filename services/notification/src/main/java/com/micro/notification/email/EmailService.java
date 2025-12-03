package com.micro.notification.email;


import com.micro.notification.kafka.order.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.micro.notification.email.EmailTemplates.ORDER_CONFIRMATION;
import static com.micro.notification.email.EmailTemplates.PAYMENT_CONFIRMATION;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * EmailService
 *
 * Handles sending notification emails (order confirmation, payment success) to customers
 * using templated HTML content and asynchronous delivery via JavaMail.
 *
 * <b>
 *     Core Responsibilities:
 * </b>
 * <ul>
 *   <li>Sends templated email notifications using Thymeleaf HTML templates.</li>
 *   <li>Handles both payment success and order confirmation scenarios.</li>
 *   <li>Uses Spring's @Async to send emails in the background, not blocking service threads.</li>
 *   <li>Logs email delivery results or problems for tracing/debugging.</li>
 * </ul>
 *
 * <b>Injected Dependencies:</b>
 * <ul>
 *   <li><b>JavaMailSender:</b> Used to construct and send MIME emails via SMTP.</li>
 *   <li><b>SpringTemplateEngine:</b> Provides Thymeleaf template rendering for HTML email bodies.</li>
 * </ul>
 *
 * <b>Annotations Used:</b>
 * <ul>
 *   <li><b>@Service:</b> Makes this a Spring bean, available for injection.</li>
 *   <li><b>@Slf4j:</b> Adds SLF4J logger 'log' field for logging operations.</li>
 *   <li><b>@RequiredArgsConstructor:</b> Lombok: generates constructor for final dependencies.</li>
 *   <li><b>@Async:</b> Marks methods for asynchronous execution.</li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    /**
     * Sends a payment success email to the specified customer.
     * Uses a Thymeleaf template for the email body. Executes asynchronously.
     *
     * @param destinationEmail  Customer email address
     * @param customerName      Customer full name
     * @param amount            Payment amount
     * @param orderReference    Reference code for the order
     * @throws MessagingException If email delivery fails
     */
    @Async
    public void sendPaymentSuccessEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference
    ) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
        messageHelper.setFrom("mahmuud.h.20@gmail.com");

        final String templateName = PAYMENT_CONFIRMATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("amount", amount);
        variables.put("orderReference", orderReference);

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(PAYMENT_CONFIRMATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info(String.format("INFO - Email successfully sent to %s with template %s ", destinationEmail, templateName));
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send Email to {} ", destinationEmail);
        }

    }
    /**
     * Sends an order confirmation email to the specified customer, including product details.
     * Uses a Thymeleaf template for the email body. Executes asynchronously.
     *
     * @param destinationEmail  Customer email address
     * @param customerName      Customer full name
     * @param amount            Total order amount
     * @param orderReference    Reference code for the order
     * @param products          List of ordered products
     * @throws MessagingException If email delivery fails
     */
    @Async
    public void sendOrderConfirmationEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference,
            List<Product> products
    ) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
        messageHelper.setFrom("contact@aliboucoding.com");

        final String templateName = ORDER_CONFIRMATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("totalAmount", amount);
        variables.put("orderReference", orderReference);
        variables.put("products", products);

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(ORDER_CONFIRMATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info(String.format("INFO - Email successfully sent to %s with template %s ", destinationEmail, templateName));
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send Email to {} ", destinationEmail);
        }

    }
}
