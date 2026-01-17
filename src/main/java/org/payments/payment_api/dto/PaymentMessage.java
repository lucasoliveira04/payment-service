package org.payments.payment_api.dto;

import lombok.Builder;

@Builder
public record PaymentMessage(
        String paymentMethod,
        PaymentProcessRequestDto payment
) {}

