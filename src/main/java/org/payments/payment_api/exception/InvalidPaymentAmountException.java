package org.payments.payment_api.exception;

public class InvalidPaymentAmountException extends RuntimeException {
    public InvalidPaymentAmountException(Integer amount) {
        super("Payment amount " + amount + " is out of allowed range");
    }
}
