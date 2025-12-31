package org.payments.payment_api.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String PAYMENT_QUEUE = "payment_queue";
    public static final String PAYMENT_QUEUE_LOG = "payment_queue_log";

    @Bean
    public Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE, true, false, false);
    }

    @Bean
    public Queue paymentQueueLog() {
        return new Queue(PAYMENT_QUEUE_LOG, true, false, false);
    }

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
