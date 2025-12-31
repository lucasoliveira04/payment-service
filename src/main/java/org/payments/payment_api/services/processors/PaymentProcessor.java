package org.payments.payment_api.services.processors;

import org.payments.payment_api.dto.PaymentRequestDto;
import org.payments.payment_api.enums.PaymentMethodEnum;

public interface PaymentProcessor {
    void process(PaymentRequestDto payment) throws Exception;
    String getServiceName();
    boolean supports(PaymentMethodEnum method);
}
