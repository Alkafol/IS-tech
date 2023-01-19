package com.techprog.outerinterfacesmicroservice.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.techprog.entities.RabbitConstants.*;


@Configuration
public class ClientConfig {

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(APP_EXCHANGE);
    }
}
