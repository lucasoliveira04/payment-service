package org.payments.payment_api.services.processors;

import com.stripe.model.Charge;
import com.stripe.model.PaymentMethod;
import com.stripe.param.ChargeCreateParams;
import org.payments.payment_api.dto.PaymentRequestDto;
import org.payments.payment_api.enums.PaymentMethodEnum;
import org.payments.payment_api.exception.FormPaymentException;
import org.payments.payment_api.services.StripePaymentImpl;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class StripeProcessor implements PaymentProcessor {

    @Override
    public void process(PaymentRequestDto payment) throws Exception {
        ChargeCreateParams params = ChargeCreateParams.builder()
                .setAmount(payment.amount())
                .setCurrency(payment.currency())
                .setDescription(payment.description())
                .setSource(payment.token())
                .build();

        Charge.create(params);
    }

    @Override
    public String getServiceName() {
        return StripePaymentImpl.class.getAnnotation(Service.class).value();
    }

    @Override
    public boolean supports(PaymentMethodEnum method) {
        switch (method) {
            case CREDIT, DEBIT, BOLETO:
                return true;
            default:
                throw new FormPaymentException(method.name());
        }
    }
}