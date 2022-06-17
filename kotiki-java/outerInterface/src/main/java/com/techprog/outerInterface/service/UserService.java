package com.techprog.outerInterface.service;

import com.rabbitmq.client.*;
import com.techprog.outerInterface.models.User;
import com.techprog.outerInterface.repository.UserRepository;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.util.SerializationUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserService {
    private static final String USERS_QUEUE = "users_queue";
    private static final String USERS_RESPONSE_QUEUE = "users_response_queue";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private void messageHandler() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(USERS_QUEUE, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        };

        String request = channel.basicConsume(USERS_QUEUE, true, deliverCallback, consumerTag -> { });
        String[] splitRequest = request.split("\\s+");
        String command = splitRequest[0];
        switch (command){
            case("get_by_username"):
                String username = (String) SerializationUtils.deserialize(splitRequest[1].getBytes(StandardCharsets.UTF_8));
                User user = userRepository.findByUsername(username);
                sendRequest(USERS_RESPONSE_QUEUE, null, user, "");
                break;
        }
    }

    private Object sendRequest(String queueName, String responseQueueName, Object obj, String command) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(responseQueueName, false, false, false, null);

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .replyTo(responseQueueName)
                .build();

        byte[] data = (command.getBytes(StandardCharsets.UTF_8).toString() + " " + org.springframework.amqp.utils.SerializationUtils.serialize(obj).toString()).getBytes(StandardCharsets.UTF_8);

        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicPublish("", queueName, props, data);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String reply = new String(delivery.getBody(), "UTF-8");
        };


        String response = channel.basicConsume(responseQueueName, true, deliverCallback, consumerTag -> { });
        return org.springframework.amqp.utils.SerializationUtils.deserialize(response.getBytes(StandardCharsets.UTF_8));
    }

}
