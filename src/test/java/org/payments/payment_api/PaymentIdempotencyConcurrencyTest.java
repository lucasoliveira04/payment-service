package org.payments.payment_api;

import org.junit.jupiter.api.Test;
import org.payments.payment_api.dto.PaymentProcessRequestDto;
import org.payments.payment_api.enums.PaymentMethodEnum;
import org.payments.payment_api.repository.PaymentRepository;
import org.payments.payment_api.service.PaymentProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PaymentIdempotencyConcurrencyTest {

    @Autowired
    PaymentProcessService service;

    @Autowired
    PaymentRepository paymentRepository;

    @Test
    void shouldCreateOnlyOnePaymentWithSameIdempotencyKey() throws Exception {
        int threads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        PaymentProcessRequestDto dto = new PaymentProcessRequestDto(
                50L,
                "BRL",
                "Pagamento concorrente",
                "tok_visa",
                PaymentMethodEnum.DEBIT,
                "order-thread-test"
        );

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    service.processPayment(dto, PaymentMethodEnum.DEBIT.toString());
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long count = paymentRepository
                .countByIdempotencyKey("order-thread-test");

        assertEquals(1, count);
    }
}

