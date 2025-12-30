package org.payments.payment_api.dto;

public record PaymentFullRequest(
        String paymentMethod,
        PaymentRequestDto payment
) {
}
