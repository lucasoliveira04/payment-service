package org.payments.payment_api.mapper;

import org.payments.payment_api.dto.PaymentProcessRequestDto;
import org.payments.payment_api.model.Payment;

public class PaymentMapper {

    public static Payment toEntity(PaymentProcessRequestDto dto) {
        return Payment.builder()
                .amount(dto.amount())
                .currency(dto.currency())
                .description(dto.description())
                .token(dto.token())
                .method(dto.method())
                .idempotencyKey(dto.idempotencyKey())
                .build();
    }
}
