package org.payments.payment_api.workers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.payment_api.dto.PaymentMessage;
import org.payments.payment_api.services.processors.PaymentProcessor;
import org.payments.payment_api.services.processors.PaymentProcessorFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentWorker {

    private static final String PAYMENT_QUEUE = "payment_queue";
    private final PaymentProcessorFactory factory;

    @RabbitListener(queues = PAYMENT_QUEUE)
    public void listen(PaymentMessage message) {

        log.info("Processing payment using {}", message.paymentMethod());

        try {
            PaymentProcessor processor =
                    factory.get(message.paymentMethod(), message.payment().method());

            processor.process(message.payment());

            log.info("Payment processed successfully");

        } catch (Exception e) {
            log.error("Payment failed", e);
        }
    }
}
