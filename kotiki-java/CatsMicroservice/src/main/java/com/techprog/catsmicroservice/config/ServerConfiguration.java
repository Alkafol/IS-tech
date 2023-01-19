package com.techprog.catsmicroservice.config;

import org.springframework.amqp.core.*;
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
    public Queue addCatQueue() {
        return new Queue(ADD_CAT_QUEUE);
    }

    @Bean
    public Queue getByIdQueue(){
        return new Queue(GET_CAT_QUEUE);
    }

    @Bean
    public Queue getAllCats(){
        return new Queue(GET_ALL_CATS_QUEUE);
    }

    @Bean
    public Queue startFriendship(){
        return new Queue(START_FRIENDSHIP_QUEUE);
    }

    @Bean
    public Queue getColoredQueue(){
        return new Queue(GET_CATS_BY_COLOR_QUEUE);
    }

    @Bean
    public Queue stopFriendship(){
        return new Queue(STOP_FRIENDSHIP_QUEUE);
    }

    @Bean
    public Queue deleteCat(){
        return new Queue(DELETE_CAT_QUEUE);
    }

    @Bean
    public Binding bindingAddCat(DirectExchange directExchange, Queue addCatQueue) {
        return BindingBuilder.bind(addCatQueue).to(directExchange).with(ADD_CAT_QUEUE);
    }

    @Bean
    public Binding bindingGetById(DirectExchange directExchange, Queue getByIdQueue){
        return BindingBuilder.bind(getByIdQueue).to(directExchange).with(GET_CAT_QUEUE);
    }

    @Bean
    public Binding bingingGetAllCats(DirectExchange directExchange, Queue getAllCats){
        return BindingBuilder.bind(getAllCats).to(directExchange).with(GET_ALL_CATS_QUEUE);
    }

    @Bean
    public Binding bindingStartFriendship(DirectExchange directExchange, Queue startFriendship){
        return BindingBuilder.bind(startFriendship).to(directExchange).with(START_FRIENDSHIP_QUEUE);
    }

    @Bean
    public Binding bindingStopFriendship(DirectExchange directExchange, Queue stopFriendship){
        return BindingBuilder.bind(stopFriendship).to(directExchange).with(STOP_FRIENDSHIP_QUEUE);
    }

    @Bean
    public Binding bindingDeleteCat(DirectExchange directExchange, Queue deleteCat){
        return BindingBuilder.bind(deleteCat).to(directExchange).with(DELETE_CAT_QUEUE);
    }

    @Bean
    public Binding bindingGetColored(DirectExchange directExchange, Queue getColoredQueue){
        return BindingBuilder.bind(getColoredQueue).to(directExchange).with(GET_CATS_BY_COLOR_QUEUE);
    }
}