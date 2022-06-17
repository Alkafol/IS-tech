package com.techprog.cat.service;

import com.rabbitmq.client.*;
import com.responseModels.OwnerResponse;
import com.responseModels.UserResponse;
import com.techprog.cat.CatMessagingConfig;
import com.techprog.cat.dto.CatDto;
import com.techprog.cat.models.Cat;
import com.techprog.cat.models.Color;
import com.techprog.cat.repository.CatRepository;
import com.techprog.cat.tools.CatExistenceException;
import com.techprog.cat.tools.CatOwnershipException;
import com.techprog.cat.tools.OwnerAccessibilityException;
import com.techprog.cat.tools.OwnerExistenceException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class CatsService {
    private static final String CATS_QUEUE = "cats_queue";
    private static final String CATS_RESPONSE_QUEUE = "cats_response_queue";
    private static final String USERS_QUEUE = "users_queue";
    private static final String USERS_RESPONSE_QUEUE = "users_response_queue";
    private static final String OWNERS_QUEUE = "cats_queue";
    private static final String OWNERS_RESPONSE_QUEUE = "cats_response_queue";

    private final CatRepository catRepository;

    public CatsService(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    public Cat addCat(CatDto catDto, String username) throws OwnerExistenceException, OwnerAccessibilityException, Exception {
        UserResponse userResponse = (UserResponse) sendRequest(USERS_QUEUE, USERS_RESPONSE_QUEUE, username, "get_by_username");
        if (userResponse.getRole() == "OWNER"){
            if (catDto.getOwnerId() == null) {
                catDto.setOwnerId(((OwnerResponse)sendRequest(OWNERS_QUEUE, OWNERS_RESPONSE_QUEUE, username, "get_by_username")).getId());
            }
            else{
                throw new OwnerAccessibilityException();
            }
        }
        Cat cat = this.convertToCat(catDto);
        catRepository.save(cat);
        return cat;
    }

    public CatDto getCatById(String id, String username) throws CatOwnershipException, Exception {
        UserResponse userResponse = (UserResponse) sendRequest(USERS_QUEUE, USERS_RESPONSE_QUEUE, username, "get_by_username");
        if (userResponse.getRole() == "OWNER"){
            if (catRepository.findById(id).get().getOwnerId() != ((OwnerResponse)sendRequest(OWNERS_QUEUE, OWNERS_RESPONSE_QUEUE, username, "get_by_username")).getId()){
                throw new CatOwnershipException();
            }
        }
        return this.convertToCatDto(catRepository.findById(id).get());
    }

    public void deleteCatById(String id, String username) throws CatExistenceException, OwnerAccessibilityException, Exception {
        UserResponse userResponse = (UserResponse) sendRequest(USERS_QUEUE, USERS_RESPONSE_QUEUE, username, "get_by_username");
        if (userResponse.getRole() == "OWNER"){
            if (catRepository.findById(id).get().getOwnerId() != ((OwnerResponse) sendRequest(OWNERS_QUEUE, OWNERS_RESPONSE_QUEUE, username, "get_by_username")).getId()){
                throw new OwnerAccessibilityException();
            }
        }
        OwnerResponse owner = (OwnerResponse) sendRequest(OWNERS_QUEUE, OWNERS_RESPONSE_QUEUE, getCatById(id, username).getOwnerId(), "get_by_id");
        if (catRepository.getById(id).getFriends().size() != 0){
            for (int i = 0; i < catRepository.getById(id).getFriends().size(); i++){
                catRepository.getById(id).getFriends().stream().toList().get(i).removeFriend(catRepository.getById(id));
                catRepository.save(catRepository.getById(id).getFriends().stream().toList().get(i));
            }
            catRepository.getById(id).removeAllFriends();
        }
        sendRequest(OWNERS_QUEUE, null, catRepository.getById(id), "remove_cat");
        catRepository.save(catRepository.getById(id));
        sendRequest(OWNERS_QUEUE, null, owner, "save");
    }

    public void startFriendship(String firstCatId, String secondCatId) throws CatExistenceException {

        Cat firstCat = catRepository.getById(firstCatId);
        Cat secondCat = catRepository.getById(secondCatId);

        if (firstCat == null || secondCat == null){
            throw new CatExistenceException();
        }

        firstCat.addFriend(secondCat);
        secondCat.addFriend(firstCat);

        catRepository.save(firstCat);
        catRepository.save(secondCat);
    }

    public void stopFriendship(String firstCatId, String secondCatId) throws CatExistenceException {
        Cat firstCat = catRepository.getById(firstCatId);
        Cat secondCat = catRepository.getById(secondCatId);

        if (firstCat == null || secondCat == null){
            throw new CatExistenceException();
        }

        firstCat.removeFriend(secondCat);
        secondCat.removeFriend(firstCat);

        catRepository.save(firstCat);
        catRepository.save(secondCat);
    }

    public Set<CatDto> getAllCatsOfOwner(String ownerId) throws OwnerExistenceException, Exception {
        OwnerResponse ownerResponse = (OwnerResponse) sendRequest(OWNERS_QUEUE, OWNERS_RESPONSE_QUEUE, ownerId, "get_by_id");
        if (ownerResponse == null){
            throw new OwnerExistenceException();
        }

        return ownerResponse.getCatsId().stream().map(catId -> {
            try {
                return (CatDto)sendRequest(CATS_QUEUE, CATS_RESPONSE_QUEUE, catId, "get_by_id");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toSet());

    }

    public List<CatDto> getAllCats(String username) throws Exception{
        UserResponse userResponse = (UserResponse) sendRequest(USERS_QUEUE, USERS_RESPONSE_QUEUE, username, "get_by_username");
        OwnerResponse ownerResponse = (OwnerResponse) sendRequest(OWNERS_QUEUE, OWNERS_RESPONSE_QUEUE, username, "get_by_username");
        if (userResponse.getRole() == "ADMIN") {
            return catRepository.findAll().stream().map(cat -> convertToCatDto(cat)).collect(Collectors.toList());
        }
        else if (userResponse.getRole() == "OWNER"){
            return ownerResponse.getCatsId().stream().map(catId -> {
                try {
                    return convertToCatDto((Cat) sendRequest(CATS_QUEUE, CATS_RESPONSE_QUEUE, catId, "get_by_username"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
        }
        return null;
    }

    public Set<CatDto> getAllCatFriends(String catId){
        Set<CatDto> allFriends = catRepository.getById(catId).getFriends().stream().map(friend -> convertToCatDto(friend)).collect(Collectors.toSet());
        return allFriends;
    }

    public List<CatDto> getColored(Color color, String username) throws Exception{
        UserResponse userResponse = (UserResponse) sendRequest(USERS_QUEUE, USERS_RESPONSE_QUEUE, username, "get_by_username");
        OwnerResponse ownerResponse = (OwnerResponse) sendRequest(OWNERS_QUEUE, OWNERS_RESPONSE_QUEUE, username, "get_by_username");
        List<Cat> cats = Collections.emptyList();
        if (userResponse.getRole() == "ADMIN") {
            cats = catRepository.findAll().stream().filter(c -> c.getColor().equals(color)).collect(Collectors.toList());
        }
        if (userResponse.getRole() == "OWNER"){
            cats = catRepository.findAll().stream().filter(c -> c.getColor().equals(color)).filter(c -> c.getOwnerId() == ownerResponse.getId()).collect(Collectors.toList());
        }
        List<CatDto> cats_dto = cats.stream().map(cat -> convertToCatDto(cat)).collect(Collectors.toList());
        return cats_dto;
    }

    public CatDto convertToCatDto(Cat cat){
        CatDto catDto = new CatDto();
        catDto.setName(cat.getName());
        catDto.setBreed(cat.getBreed());
        catDto.setId(cat.getId());
        catDto.setDateOfBirth(cat.getDateOfBirth());
        if (cat.getOwnerId() != null) {
            OwnerResponse ownerResponse = null;
            try {
                ownerResponse = (OwnerResponse) sendRequest(OWNERS_QUEUE, OWNERS_RESPONSE_QUEUE, cat.getId(), "get_by_catId");
            } catch (Exception e) {
                e.printStackTrace();
            }
            catDto.setOwnerId(ownerResponse.getId());
        }
        else{
            catDto.setOwnerId(null);
        }
        catDto.setFriendsId(cat.getFriends().stream().map(friend -> friend.getId()).collect(Collectors.toList()));
        catDto.setColor(cat.getColor().toString());
        return catDto;
    }

    public Cat convertToCat(CatDto catDto) throws OwnerExistenceException {
        String name = catDto.getName();
        String breed = catDto.getBreed();
        String id = catDto.getId();
        Color color = Color.valueOf(catDto.getColor());
        if (catDto.getOwnerId() == null){
            throw new OwnerExistenceException();
        }
        String ownerId = catDto.getOwnerId();
        Calendar dateOfBirth = catDto.getDateOfBirth();
        Set<Cat> friends = new HashSet<>();
        if (!catDto.getFriendsId().isEmpty()) {
            friends = catDto.getFriendsId().stream().map(friendId -> catRepository.findById(friendId).get()).collect(Collectors.toSet());
        }
        Cat cat = Cat.createCustomCat(name, id, dateOfBirth, breed, color, ownerId, friends);
        return cat;
    }

    private void messageHandler() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(CATS_QUEUE, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        };

        String request = channel.basicConsume(CATS_QUEUE, true, deliverCallback, consumerTag -> { });
        String[] splitRequest = request.split("\\s+");
        String command = splitRequest[0];
        switch (command){
            case("add"):
                CatDto cat = (CatDto) SerializationUtils.deserialize(splitRequest[1].getBytes(StandardCharsets.UTF_8));
                addCat(cat, splitRequest[2]);
                break;
            case("get_all"):
                List<CatDto> cats = getAllCats(splitRequest[1]);
                sendRequest(CATS_RESPONSE_QUEUE, null, cats, "");
                break;
            case("get_by_id"):
                CatDto catById = getCatById(splitRequest[1], splitRequest[2]);
                sendRequest(CATS_RESPONSE_QUEUE, null, catById, "");
                break;
            case("delete"):
                deleteCatById(splitRequest[1], splitRequest[2]);
                break;
            case("start_friendship"):
                startFriendship(splitRequest[1], splitRequest[2]);
                break;
            case("stop_friendship"):
                stopFriendship(splitRequest[1], splitRequest[2]);
                break;
            case("get_by_color"):
                getColored(Color.valueOf(splitRequest[1]), splitRequest[2]);
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

