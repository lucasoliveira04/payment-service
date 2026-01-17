package org.payments.payment_api.producer;

import org.payments.payment_api.dto.PaymentProcessRequestDto;

public interface PaymentProducer {
    void send(PaymentProcessRequestDto dto);
}
