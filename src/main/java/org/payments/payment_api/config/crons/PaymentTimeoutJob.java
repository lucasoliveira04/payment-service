package org.payments.payment_api.config.crons;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.payment_api.repository.PaymentRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@EnableScheduling
@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentTimeoutJob {

    private final PaymentRepository paymentRepository;

    @Scheduled(fixedDelay = 10_000)
    @Transactional
    public void cancelPendingPayments() {
        paymentRepository.cancelExpired(
                OffsetDateTime.now().minusSeconds(10)
        );
        log.info("[PaymentTimeoutJob] Expired pending payments cancelled");
    }
}