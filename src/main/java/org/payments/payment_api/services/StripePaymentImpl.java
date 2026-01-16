package org.payments.payment_api.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.payment_api.dto.PaymentMessage;
import org.payments.payment_api.dto.PaymentRequestDto;
import org.payments.payment_api.services.processors.PaymentProcessorFactory;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static org.payments.payment_api.configuration.RabbitMqConfig.PAYMENT_QUEUE;

@Slf4j
@RequiredArgsConstructor
@Service(StripePaymentImpl.SERVICE_NAME)
public class StripePaymentImpl implements IPayment {

    public static final String SERVICE_NAME = "STRIPE";
    private static final Integer VALUE_MIN = 50;
    private static final Integer VALUE_MAX = 100;

    private final RabbitTemplate rabbitTemplate;
    private final PaymentProcessorFactory paymentProcessorFactory;

    @Override
    public void processPayment(PaymentRequestDto paymentDetails) {

        String traceId = MDC.get("traceId");

        if (paymentDetails.amount() < VALUE_MIN || paymentDetails.amount() > VALUE_MAX) {
            throw new IllegalArgumentException("Payment amount out of allowed range");
        }

        paymentProcessorFactory.get(
                SERVICE_NAME,
                paymentDetails.method()
        );

        PaymentMessage message = new PaymentMessage(SERVICE_NAME, paymentDetails);

        rabbitTemplate.convertAndSend(
                PAYMENT_QUEUE,
                message,
                msg -> {
                    if (traceId != null) {
                        msg.getMessageProperties().setHeader("traceId", traceId);
                    }
                    return msg;
                }
        );

        log.info(
                "Payment message sent to queue: {} | traceId={} | parentSpanId={}",
                PAYMENT_QUEUE
        );
    }
}
