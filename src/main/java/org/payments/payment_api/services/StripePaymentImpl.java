package org.payments.payment_api.services;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.payments.payment_api.dto.PaymentRequestDto;
import org.springframework.stereotype.Service;

@Slf4j
@Service("stripePayment")
public class StripePaymentImpl implements IPayment{

    private static final Integer VALUE_MIN = 50;
    private static final Integer VALUE_MAX = 100;

    @Override
    public void processPayment(PaymentRequestDto paymentDetails) {

        if (paymentDetails.amount() < VALUE_MIN || paymentDetails.amount() > VALUE_MAX) {
            log.warn("Payment amount " + paymentDetails.amount() + " is out of allowed range.");
            return;
        }

        try {
            ChargeCreateParams params = ChargeCreateParams.builder()
                    .setAmount(paymentDetails.amount())
                    .setCurrency(paymentDetails.currency())
                    .setDescription(paymentDetails.description())
                    .setSource(paymentDetails.token())
                    .build();

            Charge charge = Charge.create(params);

            log.info("Charge created successfully with ID: " + charge.getId());
        } catch (StripeException e){
            e.printStackTrace();
        }
    }
}
