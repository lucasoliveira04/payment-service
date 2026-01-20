package org.payments.payment_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.payment_api.dto.PaymentProcessRequestDto;
import org.payments.payment_api.enums.StatusPayment;
import org.payments.payment_api.mapper.PaymentMapper;
import org.payments.payment_api.model.Payment;
import org.payments.payment_api.repository.PaymentRepository;
import org.payments.payment_api.service.producer.PaymentProducerResolver;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentProcessService {

    private final PaymentProducerResolver paymentProducerResolver;
    private final PaymentRepository paymentRepository;

    public void processPayment(PaymentProcessRequestDto dto, String processorType) {

        String idempotencyKey = dto.idempotencyKey() != null
                ? dto.idempotencyKey()
                : UUID.randomUUID().toString();

        PaymentProcessRequestDto effectiveDto =
                dto.idempotencyKey() != null
                        ? dto
                        : new PaymentProcessRequestDto(
                        dto.amount(),
                        dto.currency(),
                        dto.description(),
                        dto.token(),
                        dto.method(),
                        idempotencyKey
                );

        paymentRepository.findByIdempotencyKey(idempotencyKey)
                .ifPresent(existing -> {
                    log.info("Idempotent retry for key {}", idempotencyKey);
                    throw new IllegalArgumentException("Payment already exists for idempotency key: " + idempotencyKey);
                });

        Payment payment = PaymentMapper.toEntity(effectiveDto);

        payment.setStatus(StatusPayment.PENDING);
        paymentRepository.save(payment);

        log.info("[Service] Payment saved with ID: {}", payment.getId());

        paymentProducerResolver
                .resolve(processorType)
                .send(effectiveDto);

        log.info("[Service] Payment message sent to producer for processor: {}", processorType);
    }

}
