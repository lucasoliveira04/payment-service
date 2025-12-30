package org.payments.payment_api.services;

import org.payments.payment_api.dto.PaymentRequestDto;

public interface IPayment {
    void processPayment(PaymentRequestDto paymentDetails);
}
