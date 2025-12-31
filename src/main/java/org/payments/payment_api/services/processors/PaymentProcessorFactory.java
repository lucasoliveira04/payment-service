package org.payments.payment_api.services.processors;

import org.payments.payment_api.enums.PaymentMethodEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentProcessorFactory {
    private final Map<String, PaymentProcessor> processors;

    public PaymentProcessorFactory(List<PaymentProcessor> processors) {
        this.processors = processors.stream()
                .collect(Collectors.toMap(
                        PaymentProcessor::getServiceName,
                        Function.identity()
                ));
    }

    public PaymentProcessor get(String serviceName, PaymentMethodEnum method) {
        PaymentProcessor processor = processors.get(serviceName);
        if (processor == null) {
            throw new IllegalArgumentException("Unsupported payment service: " + serviceName);
        }

        if (!processor.supports(method)){
            throw new IllegalArgumentException("Payment method not supported: " + method);
        }

        return processor;
    }
}
