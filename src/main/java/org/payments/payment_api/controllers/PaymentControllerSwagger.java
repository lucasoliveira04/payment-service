package org.payments.payment_api.controllers;

import org.payments.payment_api.dto.PaymentProcessRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface PaymentControllerSwagger {

    @PostMapping("/stripe")
    public ResponseEntity<String> processPayment(@RequestBody PaymentProcessRequestDto paymentRequest);
}
