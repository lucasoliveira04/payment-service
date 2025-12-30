package org.payments.payment_api.dto;

import java.io.Serializable;

public record PaymentRequestDto(
        Long amount,
        String currency,
        String description,
        String token
) implements Serializable {
}
