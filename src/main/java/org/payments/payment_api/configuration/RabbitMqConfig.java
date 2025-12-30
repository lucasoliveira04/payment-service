package org.payments.payment_api.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    private static final String EXCHANGE_NAME = "payment_exchange";

    @Bean
    public Queue paymentQueue() {
        return new Queue("payment_queue", true);
    }
}
