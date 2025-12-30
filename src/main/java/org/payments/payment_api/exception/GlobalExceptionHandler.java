package org.payments.payment_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidPaymentAmountException.class)
    public ResponseEntity<String> handleInvalidAmount(InvalidPaymentAmountException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }

    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<String> handlePaymentProcessing(PaymentProcessingException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body("Payment provider error");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidRequest(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .badRequest()
                .body("Malformed request");
    }
}
