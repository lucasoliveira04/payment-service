package org.payments.payment_api.service.processor;

import org.payments.payment_api.dto.PaymentProcessRequestDto;

public interface PaymentGatewayProcessor {
    void process(PaymentProcessRequestDto dto);
}
