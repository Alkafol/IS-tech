package com.techprog.cat;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.core.*;


@Configuration
public class CatMessagingConfig {

    public static final String CAT_QUEUE = "cat_queue";
    public static final String CAT_EXCHANGE = "cat_exchange";
    public static final String CAT_ROUTING_KEY = "cat_routing_key";

    @Bean
    public Queue queue(){
        return new Queue(CAT_QUEUE);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(CAT_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(CAT_ROUTING_KEY);
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
