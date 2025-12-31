package org.payments.payment_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.payments.payment_api.enums.PaymentMethodEnum;

public record PaymentRequestDto(
        Long amount,
        String currency,
        String description,
        String token,

        @JsonProperty("payment_method")
        PaymentMethodEnum method
) {
}
