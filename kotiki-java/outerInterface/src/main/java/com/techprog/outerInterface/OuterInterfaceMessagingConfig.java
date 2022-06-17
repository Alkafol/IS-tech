package com.techprog.outerInterface;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.core.*;


@Configuration
public class OuterInterfaceMessagingConfig {

    public static final String OUTER_INTERFACE_QUEUE = "outer_interface_queue";
    public static final String OUTER_INTERFACE_EXCHANGE = "outer_interface_exchange";
    public static final String OUTER_INTERFACE_ROUTING_KEY = "outer_interface_routing_key";

    @Bean
    public Queue queue(){
        return new Queue(OUTER_INTERFACE_QUEUE);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(OUTER_INTERFACE_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(OUTER_INTERFACE_ROUTING_KEY);
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}