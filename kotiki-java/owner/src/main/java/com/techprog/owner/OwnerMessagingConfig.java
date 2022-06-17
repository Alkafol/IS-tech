package com.techprog.owner;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.core.*;


@Configuration
public class OwnerMessagingConfig {

    public static final String OWNER_QUEUE = "owner_queue";
    public static final String OWNER_EXCHANGE = "owner_exchange";
    public static final String OWNER_ROUTING_KEY = "owner_routing_key";

    @Bean
    public Queue queue(){
        return new Queue(OWNER_QUEUE);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(OWNER_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(OWNER_ROUTING_KEY);
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
