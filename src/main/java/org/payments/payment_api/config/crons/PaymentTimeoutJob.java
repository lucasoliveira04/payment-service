package org.payments.payment_api.config.crons;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.payment_api.repository.PaymentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private final JdbcTemplate jdbcTemplate;

    @Scheduled(fixedDelay = 10_000, initialDelay = 15_000)
    @Transactional
    public void cancelPendingPayments() {
        jdbcTemplate.execute("select cancel_expired_payments()");
        log.info("[Cron Job] Executed cancel_expired_payments function to cancel pending payments");
    }
}