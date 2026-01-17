package org.payments.payment_api.config.rabbitmq;

import org.springframework.amqp.core.Queue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQQueues {

    public static final String PAYMENT_PROCESS_QUEUE = "payment.process.queue";

    @Bean
    public Queue paymentProcessQueue() {
        return new Queue(PAYMENT_PROCESS_QUEUE, true);
    }
}
