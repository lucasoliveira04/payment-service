package org.payments.payment_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.payment_api.dto.PaymentRequestDto;
import org.payments.payment_api.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payments/stripe")
public class PaymentStripeController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Void> processPayment(
            @RequestBody PaymentRequestDto paymentRequestDto
    ) {
        log.info("Received payment request with method {}", "STRIPE");

        paymentService.processPayment(paymentRequestDto, "STRIPE");

        return ResponseEntity.accepted().build();
    }
}