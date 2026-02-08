package org.payments.payment_api.config.rabbitmq;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitHealthConfig {

    @Bean
    public HealthIndicator rabbitHealthIndicator(){
        return () -> Health
                .down()
                .withDetail("rabbit", "Indisponível no momento")
                .build();
    }
}
