package org.payments.payment_api.workers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.payment_api.config.rabbitmq.RabbitMQQueues;
import org.payments.payment_api.dto.PaymentMessage;
import org.payments.payment_api.dto.PaymentProcessRequestDto;
import org.payments.payment_api.enums.StatesPayment;
import org.payments.payment_api.model.Payment;
import org.payments.payment_api.repository.PaymentRepository;
import org.payments.payment_api.service.processor.PaymentProcessorResolver;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProcessWorker {

    private final PaymentProcessorResolver processorResolver;
    private final ObjectMapper objectMapper;
    private final PaymentRepository paymentRepository;

    @RabbitListener(queues = RabbitMQQueues.PAYMENT_PROCESS_QUEUE)
    public void listen(Message message) throws Exception {
        log.info("[Worker] Received payment processing message from RabbitMQ");

        String traceId = (String) message.getMessageProperties().getHeaders().get("X-Trace-Id");
        if (traceId != null) {
            MDC.put("traceId", traceId);
        }

        PaymentMessage paymentMessage = objectMapper.readValue(message.getBody(), PaymentMessage.class);

        PaymentProcessRequestDto dto = paymentMessage.payment();

        Payment payment = paymentRepository.findByIdempotencyKey(dto.idempotencyKey())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found for idempotency key: " + dto.idempotencyKey()));

        if (payment.getState() == StatesPayment.SUCCESS){
            log.info("[Worker] Payment already processed successfully for idempotency: {}. Skipping.", dto.idempotencyKey());
            return;
        }

        if (payment.getState() == StatesPayment.PROCESSING){
            log.info("[Worker] Payment is already in PROCESSING state for idempotency: {}. Skipping.", dto.idempotencyKey());
            return;
        }

        try {
            payment.setState(StatesPayment.PROCESSING);
            paymentRepository.save(payment);

            log.info("[Worker] Payment state updated to PROCESSING for idempotency: {}", dto.idempotencyKey());

            processorResolver
                    .get(paymentMessage.paymentMethod())
                    .process(dto);

            payment.setState(StatesPayment.SUCCESS);
            paymentRepository.save(payment);
            log.info("[Worker] Payment processed successfully for idempotency: {}", dto.idempotencyKey());
            log.info("[Worker] Key idempotency: {}", dto.idempotencyKey());
        } catch (Exception e) {
            log.error("[Worker] Error processing payment for idempotency: {}: {}", dto.idempotencyKey(), e.getMessage());
            payment.setState(StatesPayment.FAILED);
            paymentRepository.save(payment);
        } finally {
            log.info("[Worker] Finalizing payment processing for idempotency: {}", dto.idempotencyKey());
            MDC.clear();
        }
    }

}
