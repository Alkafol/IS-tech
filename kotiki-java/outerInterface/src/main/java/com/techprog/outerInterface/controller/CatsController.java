package com.techprog.outerInterface.controller;

import com.techprog.outerInterface.dto.CatDto;
import com.techprog.outerInterface.dto.OwnerDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/cats")
public class CatsController {

    private final RabbitTemplate rabbitTemplate;
    private final ConnectionFactory factory = new ConnectionFactory();
    private static final String CATS_QUEUE = "cats_queue";
    private static final String OWNERS_QUEUE = "owners_queue";
    private static final String CATS_RESPONSE_QUEUE = "cats_response_queue";
    private static final String OWNERS_RESPONSE_QUEUE = "owners_response_queue";

    public CatsController(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/cat")
    @PreAuthorize("hasAuthority('cat:get_all')")
    public List<CatDto> getAllCats() throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Object response = sendRequest(CATS_QUEUE, CATS_RESPONSE_QUEUE, username, "get_all");
        return (List<CatDto>)response;
    }

    @GetMapping("/cat/{id}")
    @PreAuthorize("hasAuthority('cat:get_by_id')")
    public CatDto getCatById(@PathVariable String id) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // try {
            // return catsService.getCatById(id, username);

            Object response = sendRequest(CATS_QUEUE, CATS_RESPONSE_QUEUE, id + username, "get_by_id");
            return (CatDto) response;
       /* }
        catch (CatOwnershipException e){
            System.out.println("Logged user doesn't have access to this cat");
            return null;
        }*/
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/cat")
    @PreAuthorize("hasAuthority('cat:add')")
    public CatDto addCat(@RequestBody CatDto cat) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // try {
            // return catsService.addCat(cat, username);

            Object response = sendRequest(CATS_QUEUE, CATS_RESPONSE_QUEUE,  cat + username, "add");
            return (CatDto) response;

        /*}
        catch (OwnerExistenceException e){
            System.out.println("Owner doesn't exist");
        }
        catch (OwnerAccessibilityException e){
            System.out.println("Logged user doesn't have permission to define owner id");
        }*/
        // return null;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/cat/{id}")
    @PreAuthorize("hasAuthority('cat:delete')")
    public void deleteCatById(@PathVariable String id) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // try {
            // catsService.deleteCatById(id, username);
            sendRequest(CATS_QUEUE, CATS_RESPONSE_QUEUE, id + username, "delete");
        /*}
        catch (CatExistenceException e){
            System.out.println("Cat doesn't exist");
        }
        catch (OwnerAccessibilityException e){
            System.out.println("Logged user doesn't have permission to delete this cat");
        }*/
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/owner")
    @PreAuthorize("hasAuthority('owner:add')")
    public OwnerDto addOwner(@RequestBody OwnerDto owner) throws Exception{

        Object response = sendRequest(OWNERS_QUEUE, CATS_RESPONSE_QUEUE, owner, "add");
        return (OwnerDto) response;

        // return ownerService.addOwner(owner);
    }

    @GetMapping("/owner/{id}")
    @PreAuthorize("hasAuthority('owner:get_by_id')")
    public OwnerDto getOwnerById(@PathVariable String id) throws Exception{

        Object response = sendRequest(OWNERS_QUEUE, OWNERS_RESPONSE_QUEUE, id, "get_by_id");
        return (OwnerDto) response;
        // return ownerService.getOwnerById(id);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/owner/{id}")
    @PreAuthorize("hasAuthority('owner:delete')")
    public void deleteOwnerById(@PathVariable String id) throws Exception{
        // try {
            sendRequest(OWNERS_QUEUE, OWNERS_RESPONSE_QUEUE, id, "delete");
            // ownerService.deleteOwnerById(id);
        /*}
        catch (OwnerExistenceException e){
        }*/
    }

    @GetMapping("/owner")
    @PreAuthorize("hasAuthority('owner:get_all')")
    public List<OwnerDto> getAllOwners() throws Exception{

        Object response = sendRequest(OWNERS_QUEUE, OWNERS_RESPONSE_QUEUE, "", "get_all_owners");
        return (List<OwnerDto>) response;

        // return ownerService.getAllOwners();
    }

    @PutMapping("/cat/startFriendship/{firstCatId}/{secondCatId}")
    @PreAuthorize("hasAuthority('cat:start_friendship')")
    public void startFriendship(@PathVariable String firstCatId, @PathVariable String secondCatId) throws Exception{
        // try {
            sendRequest(CATS_QUEUE, CATS_RESPONSE_QUEUE,  firstCatId + secondCatId, "start_friendship");

            // catsService.startFriendship(firstCatId, secondCatId);
        /*}
        catch (CatExistenceException e){
        }*/
    }

    @PutMapping("/cat/stopFriendship/{firstCatId}/{secondCatId}")
    @PreAuthorize("hasAuthority('cat:stop_friendship')")
    public void stopFriendship(@PathVariable String firstCatId, @PathVariable String secondCatId) throws Exception{
        // try {
            sendRequest(CATS_QUEUE, CATS_RESPONSE_QUEUE, firstCatId + secondCatId, "stop_friendship");
        /*}
        catch (CatExistenceException e){
        }*/
    }

    @GetMapping("/cat/getColored/{color}")
    @PreAuthorize("hasAuthority('cat:get_by_color')")
    public List<CatDto> getAllColoredCat(@PathVariable String color) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Object response = sendRequest(CATS_QUEUE, CATS_RESPONSE_QUEUE, color, "get_by_color");
        return (List<CatDto>) response;

        // return catsService.getColored(color, username);
    }

    private Object sendRequest(String queueName, String responseQueueName, Object obj, String command) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(responseQueueName, false, false, false, null);

        BasicProperties props = new BasicProperties
                .Builder()
                .replyTo(responseQueueName)
                .build();

        byte[] data = (command.getBytes(StandardCharsets.UTF_8).toString() + " " + SerializationUtils.serialize(obj).toString()).getBytes(StandardCharsets.UTF_8);

        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicPublish("", queueName, props, data);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String reply = new String(delivery.getBody(), "UTF-8");
        };


        String response = channel.basicConsume(responseQueueName, true, deliverCallback, consumerTag -> { });
        return SerializationUtils.deserialize(response.getBytes(StandardCharsets.UTF_8));
    }
}
