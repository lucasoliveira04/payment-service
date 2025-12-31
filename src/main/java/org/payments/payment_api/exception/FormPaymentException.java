package org.payments.payment_api.exception;

public class FormPaymentException extends RuntimeException {
    public FormPaymentException(String formPayment) {
        super("This payment form is invalid: " + formPayment);
    }
}
