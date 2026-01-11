package org.payments.payment_api.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.payment_api.dto.PaymentRequestDto;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentFactory paymentFactory;

    public void processPayment(PaymentRequestDto paymentRequestDto, String paymentMethod) {
        log.info("Processing payment using {}", paymentRequestDto);

        try {
            IPayment payment = paymentFactory.getPayment(paymentMethod);

            payment.processPayment(paymentRequestDto);

            log.info("Payment processed successfully");

        } catch (Exception e) {
            log.error("Payment failed", e);
        }
    }
}
