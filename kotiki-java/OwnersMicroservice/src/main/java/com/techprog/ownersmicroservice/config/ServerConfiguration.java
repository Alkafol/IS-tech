package com.techprog.ownersmicroservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static com.techprog.entities.RabbitConstants.*;


@Configuration
class ServerConfiguration {

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(APP_EXCHANGE);
    }

    @Bean
    public Queue queueDeleteOwnership(){
        return new Queue(DELETE_OWNERSHIP_QUEUE);
    }

    @Bean
    public Queue queueStartOwnership() {
        return new Queue(START_OWNERSHIP_QUEUE);
    }

    @Bean
    public Queue queueGetOwner() {
        return new Queue(GET_OWNER_QUEUE);
    }

    @Bean
    public Queue queueAddOwner(){
        return new Queue(ADD_OWNER_QUEUE);
    }

    @Bean
    public Queue queueDeleteOwner(){
        return new Queue(DELETE_OWNER_QUEUE);
    }

    @Bean
    public Binding bindingStartOwnership(DirectExchange directExchange,
                                            Queue queueStartOwnership) {
        return BindingBuilder.bind(queueStartOwnership)
                .to(directExchange)
                .with(START_OWNERSHIP_QUEUE);
    }

    @Bean
    public Binding bindingGetOwner(DirectExchange directExchange,
                                   Queue queueGetOwner) {
        return BindingBuilder.bind(queueGetOwner)
                .to(directExchange)
                .with(GET_OWNER_QUEUE);
    }

    @Bean
    public Binding bindingDeleteOwnership(DirectExchange directExchange, Queue queueDeleteOwnership){
        return BindingBuilder.bind(queueDeleteOwnership)
                .to(directExchange)
                .with(DELETE_OWNERSHIP_QUEUE);
    }

    @Bean
    public Binding bindingAddOwner(DirectExchange directExchange, Queue queueAddOwner){
        return BindingBuilder.bind(queueAddOwner)
                .to(directExchange)
                .with(ADD_OWNER_QUEUE);
    }

    @Bean
    public Binding bindingDeleteOwner(DirectExchange directExchange, Queue queueDeleteOwner){
        return BindingBuilder.bind(queueDeleteOwner)
                .to(directExchange)
                .with(DELETE_OWNER_QUEUE);
    }
}