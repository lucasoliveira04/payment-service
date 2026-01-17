package org.payments.payment_api.service.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.payment_api.config.rabbitmq.RabbitMQQueues;
import org.payments.payment_api.dto.PaymentMessage;
import org.payments.payment_api.dto.PaymentProcessRequestDto;
import org.payments.payment_api.enums.ProcessorEnum;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service("STRIPE_PRODUCER")
@RequiredArgsConstructor
public class StripePaymentProducer implements PaymentProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void send(PaymentProcessRequestDto dto) {
        PaymentMessage paymentMessage = new PaymentMessage(ProcessorEnum.STRIPE_PROCESSOR.toString(), dto);

        Message message = MessageBuilder.withBody(
                objectMapper.writeValueAsBytes(paymentMessage)
        ).setHeader("X-Trace-Id", MDC.get("traceId")).build();

        log.info("[Producer] Sending payment message to RabbitMQ: {}", paymentMessage);

        rabbitTemplate.send(RabbitMQQueues.PAYMENT_PROCESS_QUEUE, message);
    }
}