package org.payments.payment_api.workers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.payment_api.config.rabbitmq.RabbitMQQueues;
import org.payments.payment_api.dto.PaymentMessage;
import org.payments.payment_api.processor.PaymentProcessorFactory;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProcessWorker {

    private final PaymentProcessorFactory processorFactory;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQQueues.PAYMENT_PROCESS_QUEUE)
    public void listen(Message message) throws Exception {

        String traceId = (String) message.getMessageProperties().getHeaders().get("X-Trace-Id");
        if (traceId != null) {
            MDC.put("traceId", traceId);
        }

        PaymentMessage paymentMessage = objectMapper.readValue(message.getBody(), PaymentMessage.class);

        log.info("Consuming payment message: {}", paymentMessage);

        var processor = processorFactory.get(paymentMessage.paymentMethod());
        processor.process(paymentMessage.payment());

        log.info("Payment processed successfully");

        MDC.clear();
    }

}
