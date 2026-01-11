package org.payments.payment_api.workers;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.payment_api.dto.PaymentMessage;
import org.payments.payment_api.services.processors.PaymentProcessor;
import org.payments.payment_api.services.processors.PaymentProcessorFactory;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentWorker {

    private static final String PAYMENT_QUEUE = "payment_queue";
    private final PaymentProcessorFactory factory;

    @WithSpan("payment.worker.listen")
    @RabbitListener(queues = PAYMENT_QUEUE)
    public void listen(
            PaymentMessage message,
            @Header("traceId") String traceId
    ) {
        MDC.put("traceId", traceId);

        log.info("Processing payment using {}", message.paymentMethod());

        PaymentProcessor processor =
                factory.get(message.paymentMethod(), message.payment().method());

        try {
            processor.process(message.payment());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("Payment processed successfully");
    }
}
