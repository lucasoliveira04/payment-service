package org.payments.payment_api.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.payment_api.dto.PaymentProcessRequestDto;
import org.payments.payment_api.producer.PaymentProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments/process")
public class PaymentController {

    private final PaymentProducer stripePaymentProducer;

    @PostMapping("/stripe")
    public ResponseEntity<String> processPayment(
            @RequestBody PaymentProcessRequestDto paymentRequest
    ) {
        log.info("Received payment request: {}", paymentRequest);

        stripePaymentProducer.send(paymentRequest);

        return ResponseEntity.accepted()
                .body("Payment processing initiated");
    }
}
