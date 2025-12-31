package org.payments.payment_api.dto;

public record PaymentMessage(
        String paymentMethod,
        PaymentRequestDto payment
) {}
