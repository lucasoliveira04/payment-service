package org.payments.payment_api.service.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.payment_api.dto.PaymentProcessRequestDto;
import org.payments.payment_api.enums.PaymentMethodEnum;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service(StripeProcessorPaymentImpl.SERVICE_NAME)
public class StripeProcessorPaymentImpl implements PaymentGatewayProcessor {
    public static final String SERVICE_NAME = "STRIPE_PROCESSOR";
    private static final Integer VALUE_MIN = 50;
    private static final Integer VALUE_MAX = 100;
    private static final List<?> METHODS_OF_APYMENTS_SUPPORTED = List.of(PaymentMethodEnum.BOLETO, PaymentMethodEnum.CREDIT, PaymentMethodEnum.DEBIT);
    private final StripeGatewayProcessor stripeGatewayProcessor;

    @Override
    public void process(PaymentProcessRequestDto paymentProcessRequestDto) {
        log.info("[StripeProcessorPaymentImpl] Starting processing payment: {}", paymentProcessRequestDto);

        if (paymentProcessRequestDto.amount() < VALUE_MIN || paymentProcessRequestDto.amount() > VALUE_MAX) {
            throw new IllegalArgumentException("Amount not supported by Stripe");
        }

        if (!METHODS_OF_APYMENTS_SUPPORTED.contains(paymentProcessRequestDto.method())) {
            throw new IllegalArgumentException("Payment method not supported by Stripe");
        }

        stripeGatewayProcessor.process(paymentProcessRequestDto);

        log.info("[StripeProcessorPaymentImpl] Finished processing payment: {}", paymentProcessRequestDto);
    }
}
