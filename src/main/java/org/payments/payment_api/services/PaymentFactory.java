package org.payments.payment_api.services;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentFactory {

    private final Map<String, IPayment> paymentStrategies;

    public PaymentFactory(Map<String, IPayment> paymentStrategies) {
        this.paymentStrategies = paymentStrategies;
    }

    public IPayment getPayment(String paymentMethod){
        IPayment payment = paymentStrategies.get(paymentMethod);
        if (payment == null) return null;
        return payment;
    }
}
