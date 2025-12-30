package org.payments.payment_api.controller;

import lombok.RequiredArgsConstructor;
import org.payments.payment_api.dto.PaymentFullRequest;
import org.payments.payment_api.services.IPayment;
import org.payments.payment_api.services.PaymentFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentFactory paymentFactory;

    @PostMapping("/test")
    public ResponseEntity<String> testPayment(@RequestBody PaymentFullRequest request) {
        IPayment payment = paymentFactory.getPayment(request.paymentMethod());

        if (payment == null) return ResponseEntity
                .badRequest()
                .body("Unsupported payment method");

        payment.processPayment(request.payment());

        return ResponseEntity.ok("Payment processed successfully");
    }
}
