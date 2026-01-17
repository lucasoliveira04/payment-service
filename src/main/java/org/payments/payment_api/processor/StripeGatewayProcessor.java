package org.payments.payment_api.processor;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.payments.payment_api.dto.PaymentProcessRequestDto;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StripeGatewayProcessor implements PaymentGatewayProcessor{
    @Override
    public void process(PaymentProcessRequestDto payment) {
        log.info("Calling Stripe API with {}", payment);

        ChargeCreateParams params = ChargeCreateParams.builder()
                .setAmount(payment.amount())
                .setCurrency(payment.currency())
                .setDescription(payment.description())
                .setSource(payment.token())
                .build();

        try {
            Charge.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}
