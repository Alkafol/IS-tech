package com.techprog.ownersmicroservice.config;

import com.techprog.ownersmicroservice.dto.Mapper;
import com.techprog.ownersmicroservice.dto.OwnerDto;
import com.techprog.ownersmicroservice.dto.RabbitListenerOwnerResponse;
import com.techprog.ownersmicroservice.service.OwnerService;
import com.techprog.ownersmicroservice.tools.OwnerExistenceException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.techprog.entities.RabbitConstants.*;

@Component
public class Listener {
    private final OwnerService ownerService;
    private final Mapper mapper;

    public Listener(OwnerService ownerService, Mapper mapper) {
        this.ownerService = ownerService;
        this.mapper = mapper;
    }

    @RabbitListener(queues = START_OWNERSHIP_QUEUE, concurrency = "3")
    public RabbitListenerOwnerResponse startOwnership(List<Integer> ids){
        try {
            ownerService.addCatToOwner(ids.get(0), ids.get(1));
            return mapper.convertToRabbitListenerOwnerResponse(null, "OK");
        } catch (OwnerExistenceException e) {
            return mapper.convertToRabbitListenerOwnerResponse(null, e.getMessage());
        }
    }

    @RabbitListener(queues = GET_OWNER_QUEUE, concurrency = "3")
    public RabbitListenerOwnerResponse getOwnerById(Integer id) {
        try {
            return mapper.convertToRabbitListenerOwnerResponse(ownerService.getOwnerById(id), "OK");
        }
        catch (OwnerExistenceException e){
            return mapper.convertToRabbitListenerOwnerResponse(null, e.getMessage());
        }
    }
    @RabbitListener(queues = ADD_OWNER_QUEUE, concurrency =  "3")
    public RabbitListenerOwnerResponse addOwner(OwnerDto ownerDto){
        return mapper.convertToRabbitListenerOwnerResponse(ownerService.addOwner(ownerDto), "OK");
    }

    @RabbitListener(queues = DELETE_OWNERSHIP_QUEUE, concurrency = "3")
    public RabbitListenerOwnerResponse deleteOwnership(List<Integer> ids){
        try {
            ownerService.deleteOwnership(ids.get(0), ids.get(1));
            return mapper.convertToRabbitListenerOwnerResponse(null, "OK");
        } catch (OwnerExistenceException e) {
            return mapper.convertToRabbitListenerOwnerResponse(null, e.getMessage());
        }
    }

    @RabbitListener(queues = DELETE_OWNER_QUEUE, concurrency = "3")
    public RabbitListenerOwnerResponse deleteOwner(Integer id){
        try {
            ownerService.deleteOwnerById(id);
            return mapper.convertToRabbitListenerOwnerResponse(null, "OK");
        } catch (OwnerExistenceException e) {
            return mapper.convertToRabbitListenerOwnerResponse(null, e.getMessage());
        }
    }
}
