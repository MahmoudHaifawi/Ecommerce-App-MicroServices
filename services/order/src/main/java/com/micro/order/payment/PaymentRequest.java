package com.micro.order.payment;

import com.micro.order.customer.CustomerResponse;
import com.micro.order.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest
        (
                BigDecimal amount,
                PaymentMethod paymentMethod,
                Integer orderId,
                String orderReference,
                CustomerResponse customer

) {
}
