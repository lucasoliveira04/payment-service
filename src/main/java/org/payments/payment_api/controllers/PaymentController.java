package org.payments.payment_api.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.payment_api.dto.PaymentProcessRequestDto;
import org.payments.payment_api.enums.ProducerEnum;
import org.payments.payment_api.service.PaymentProcessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments/process")
public class PaymentController {

    private final PaymentProcessService paymentProcessService;

    @PostMapping("/stripe")
    public ResponseEntity<String> processPayment(
            @RequestBody PaymentProcessRequestDto paymentRequest
    ) {
        log.info("[Controller] Received payment processing request: {}", paymentRequest);

        paymentProcessService.processPayment(paymentRequest, ProducerEnum.STRIPE_PRODUCER.toString());

        log.info("[Controller] Payment processing request forwarded to service layer");

        return ResponseEntity.accepted()
                .body("Payment processing initiated");
    }
}
