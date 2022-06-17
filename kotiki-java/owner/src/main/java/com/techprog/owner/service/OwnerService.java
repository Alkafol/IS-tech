package com.techprog.owner.service;

import com.rabbitmq.client.*;
import com.responseModels.CatResponse;
import com.responseModels.UserResponse;
import com.techprog.outerInterface.security.ApplicationUserRole;
import com.techprog.owner.dto.OwnerDto;
import com.techprog.owner.models.Owner;
import com.techprog.owner.repository.OwnerRepository;
import com.techprog.owner.tools.CatExistenceException;
import com.techprog.owner.tools.OwnerExistenceException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class OwnerService {

    private static final String OWNERS_QUEUE = "owners_queue";
    private static final String OWNERS_RESPONSE_QUEUE = "owners_response_queue";
    private static final String USERS_QUEUE = "users_queue";
    private static final String USERS_RESPONSE_QUEUE = "users_response_queue";
    private static final String CATS_QUEUE = "cats_queue";
    private static final String CATS_RESPONSE_QUEUE = "cats_response_queue";

    private final OwnerRepository ownerRepository;
    private final RestTemplate restTemplate;

    public OwnerService(OwnerRepository ownerRepository, RestTemplate restTemplate) {
        this.ownerRepository = ownerRepository;
        this.restTemplate = restTemplate;
    }

    public Owner addOwner(OwnerDto ownerDto) {
        Owner owner = this.convertToOwner(ownerDto);
        ownerRepository.save(owner);
        return owner;
    }

    public OwnerDto getOwnerById(String id) throws Exception {
        return this.convertToOwnerDto(ownerRepository.findById(id).get());
    }

    public void deleteOwnerById(String id) throws OwnerExistenceException {
        ownerRepository.deleteById(id);
    }

    public OwnerDto getOwnerOfCat(String catId) throws Exception {
        CatResponse catResponse = (CatResponse) sendRequest(CATS_QUEUE, CATS_RESPONSE_QUEUE, catId, "get_by_id");
        if (catResponse.getId() == null) {
            throw new CatExistenceException();
        }

        return this.convertToOwnerDto(convertToOwner(getOwnerById(catResponse.getOwnerId())));
    }

    public List<OwnerDto> getAllOwners(){
        return ownerRepository.findAll().stream().map(owner -> {
            try {
                return convertToOwnerDto(owner);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    public OwnerDto convertToOwnerDto(Owner owner) throws Exception {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setName(owner.getName());
        ownerDto.setId(owner.getId());
        ownerDto.setDateOfBirth(owner.getDateOfBirth());
        ownerDto.setCatsId(owner.getCatsId());
        ownerDto.setUsername(owner.getUsername());
        UserResponse userResponse = (UserResponse) sendRequest(USERS_QUEUE, USERS_RESPONSE_QUEUE, owner.getUsername(), "get_by_username");
        ownerDto.setPassword(userResponse.getPassword());
        ownerDto.setRole(userResponse.getRole());
        return ownerDto;
    }

    public Owner convertToOwner(OwnerDto ownerDto) throws Exception {
        UserResponse userResponse = (UserResponse) sendRequest(USERS_QUEUE, USERS_RESPONSE_QUEUE, ownerDto.getUsername(), "get_by_username")
        String name = ownerDto.getName();
        Calendar dateOfBirth = ownerDto.getDateOfBirth();
        String id = ownerDto.getId();
        Set<String> catsId = new HashSet<>();
        if (!ownerDto.getCatsId().isEmpty()) {
            catsId = ownerDto.getCatsId().stream().map(catId -> getCatResponseByCatId(catId).getId()).collect(Collectors.toSet());
        }
        String userId = userResponse.getUsername();
        if (userId == null) {
            String username = ownerDto.getUsername();
            String password = passwordEncoder.encode(ownerDto.getPassword());
            ApplicationUserRole role = ApplicationUserRole.valueOf(ownerDto.getRole());
            userId = new User(username, password, role);
            userRepository.save(userId);
        }
        Owner owner = Owner.createCustomOwner(name, id, dateOfBirth, catsId, userId);
        return owner;
    }


    private void messageHandler() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(OWNERS_QUEUE, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        };

        String request = channel.basicConsume(OWNERS_QUEUE, true, deliverCallback, consumerTag -> { });
        String[] splitRequest = request.split("\\s+");
        String command = splitRequest[0];
        switch (command){
            case("add"):
                OwnerDto owner = (OwnerDto) SerializationUtils.deserialize(splitRequest[1].getBytes(StandardCharsets.UTF_8));
                addOwner(owner);
                break;
            case("get_all"):
                List<OwnerDto> owners = getAllOwners();
                sendRequest(OWNERS_RESPONSE_QUEUE, null, owners, "");
                break;
            case("get_by_id"):
                OwnerDto ownerById = getOwnerById(splitRequest[0]);
                sendRequest(OWNERS_RESPONSE_QUEUE, null, ownerById, "");
                break;
            case("delete"):
                deleteOwnerById(splitRequest[0]);
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


