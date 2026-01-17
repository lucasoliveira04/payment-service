package org.payments.payment_api.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentProcessorFactory {

    private final Map<String, PaymentGatewayProcessor> processors;

    public PaymentGatewayProcessor get(String paymentMethod) {
        PaymentGatewayProcessor processor = processors.get(paymentMethod);

        if (processor == null) {
            throw new IllegalArgumentException(
                    "Payment processor not found: " + paymentMethod
            );
        }

        return processor;
    }
}

