package org.payments.payment_api.service.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentProducerResolver {
    private final Map<String, PaymentProducer> paymentProducers;

    public PaymentProducer resolve(String producerType) {
        return Optional.ofNullable(paymentProducers.get(producerType))
                .orElseThrow(() -> new IllegalArgumentException("No PaymentProducer found for type: " + producerType));
    }
}
