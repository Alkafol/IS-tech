package com.techprog.catsmicroservice.config;

import com.techprog.catsmicroservice.dto.CatDto;
import com.techprog.catsmicroservice.dto.Mapper;
import com.techprog.catsmicroservice.dto.RabbitListenerCatListResponse;
import com.techprog.catsmicroservice.dto.RabbitListenerCatResponse;
import com.techprog.catsmicroservice.service.CatService;
import com.techprog.catsmicroservice.tools.CatExistenceException;
import com.techprog.catsmicroservice.tools.ExistedFriendshipException;
import com.techprog.catsmicroservice.tools.NonexistentFriendshipException;
import com.techprog.entities.cat.Color;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.techprog.entities.RabbitConstants.*;

@Component
public class Listener {
    private final CatService catService;
    private final Mapper mapper;

    public Listener(CatService catService, Mapper mapper) {
        this.catService = catService;
        this.mapper = mapper;
    }

    @RabbitListener(queues = ADD_CAT_QUEUE, concurrency = "3")
    public RabbitListenerCatResponse addCat(CatDto catDto) {
        return mapper.convertToRabbitListenerResponse("OK", catService.addCat(catDto));
    }

    @RabbitListener(queues = GET_CAT_QUEUE, concurrency = "3", returnExceptions = "true")
    public RabbitListenerCatResponse getById(Integer catId) {
        try {
            return mapper.convertToRabbitListenerResponse("OK", catService.getCatById(catId));
        } catch (CatExistenceException e) {
            return mapper.convertToRabbitListenerResponse(e.getMessage(), null);
        }
    }

    @RabbitListener(queues = GET_ALL_CATS_QUEUE, concurrency = "3")
    public RabbitListenerCatListResponse getAllCats() {
        return mapper.convertToRabbitListenerListResponse("OK", catService.getAllCats());
    }

    @RabbitListener(queues = START_FRIENDSHIP_QUEUE, concurrency = "3")
    public RabbitListenerCatResponse startFriendship(List<Integer> ids) {
        try {
            catService.startFriendship(ids.get(0), ids.get(1));
            return new RabbitListenerCatResponse("OK", null);
        } catch (CatExistenceException | ExistedFriendshipException e) {
            return new RabbitListenerCatResponse(e.getMessage(), null);
        }
    }

    @RabbitListener(queues = STOP_FRIENDSHIP_QUEUE, concurrency = "3")
    public RabbitListenerCatResponse stopFriendship(List<Integer> ids) {
        try {
            catService.stopFriendship(ids.get(0), ids.get(1));
            return new RabbitListenerCatResponse("OK", null);
        } catch (CatExistenceException | NonexistentFriendshipException e) {
            return new RabbitListenerCatResponse(e.getMessage(), null);
        }
    }

    @RabbitListener(queues = DELETE_CAT_QUEUE, concurrency = "3")
    public RabbitListenerCatResponse deleteCat(Integer catId) {
        try {
            catService.deleteCatById(catId);
            return new RabbitListenerCatResponse("OK", null);
        } catch (CatExistenceException | NonexistentFriendshipException e) {
            return new RabbitListenerCatResponse(e.getMessage(), null);
        }
    }

    @RabbitListener(queues = GET_CATS_BY_COLOR_QUEUE, concurrency = "3")
    public RabbitListenerCatListResponse getColoredCat(List<String> conditions) {
        if (conditions.size() == 1) {
            return mapper.convertToRabbitListenerListResponse(
                    "OK", catService.getColored(Color.valueOf(conditions.get(0)))
            );
        } else if (conditions.size() == 2) {
            return mapper.convertToRabbitListenerListResponse("OK", catService.getColoredWithSpecifiedOwner(
                    Color.valueOf(conditions.get(0)), Integer.parseInt(conditions.get(1)))
            );
        }

        return new RabbitListenerCatListResponse("Wrong input", null);
    }

}
