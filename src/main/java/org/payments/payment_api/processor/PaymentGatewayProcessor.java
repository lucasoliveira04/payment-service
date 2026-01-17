package org.payments.payment_api.processor;

import org.payments.payment_api.dto.PaymentProcessRequestDto;

public interface PaymentGatewayProcessor {
    void process(PaymentProcessRequestDto dto);
}
