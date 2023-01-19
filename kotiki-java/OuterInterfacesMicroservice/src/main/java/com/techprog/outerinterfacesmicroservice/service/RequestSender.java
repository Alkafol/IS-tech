package com.techprog.outerinterfacesmicroservice.service;

import com.techprog.entities.cat.Color;
import com.techprog.outerinterfacesmicroservice.dto.*;
import com.techprog.outerinterfacesmicroservice.tools.CatMicroserviceException;
import com.techprog.outerinterfacesmicroservice.tools.OwnerMicroserviceException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import static com.techprog.entities.RabbitConstants.*;

import java.util.List;

@Service
public class RequestSender {
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;

    public RequestSender(RabbitTemplate rabbitTemplate, DirectExchange directExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
    }

    OwnerDto getOwnerById(Integer ownerId) throws OwnerMicroserviceException {
        RabbitListenerOwnerResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                directExchange.getName(),
                GET_OWNER_QUEUE,
                ownerId,
                new ParameterizedTypeReference<>() {
                }
        );

        if (!response.getMessage().equals("OK")){
            throw new OwnerMicroserviceException(response.getMessage());
        }

        return response.getOwnerDto();
    }

    CatDto createCat(CatDto catDto) throws CatMicroserviceException {
        RabbitListenerCatResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                directExchange.getName(),
                ADD_CAT_QUEUE,
                catDto,
                new ParameterizedTypeReference<>() {
                }
        );

        if (!response.getMessage().equals("OK")){
            throw new CatMicroserviceException(response.getMessage());
        }

        return response.getCatDto();
    }

    void startOwnership(Integer catId, Integer ownerId) throws OwnerMicroserviceException {
        RabbitListenerOwnerResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                directExchange.getName(),
                START_OWNERSHIP_QUEUE,
                List.of(catId, ownerId),
                new ParameterizedTypeReference<>() {
                }
        );

        if (!response.getMessage().equals("OK")){
            throw new OwnerMicroserviceException(response.getMessage());
        }
    }

    void stopOwnership(Integer catId, Integer ownerId) throws OwnerMicroserviceException {
        RabbitListenerOwnerResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                directExchange.getName(),
                STOP_FRIENDSHIP_QUEUE,
                List.of(catId, ownerId),
                new ParameterizedTypeReference<>() {
                }
        );

        if (!response.getMessage().equals("OK")){
            throw new OwnerMicroserviceException(response.getMessage());
        }
    }


    CatDto getCatById(Integer catId) throws CatMicroserviceException {
        RabbitListenerCatResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                directExchange.getName(),
                GET_CAT_QUEUE,
                catId,
                new ParameterizedTypeReference<>() {
                }
        );

        if (!response.getMessage().equals("OK")){
            throw new CatMicroserviceException(response.getMessage());
        }

        return response.getCatDto();
    }

    List<CatDto> getAllCats() throws CatMicroserviceException {
        RabbitListenerCatListResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                directExchange.getName(),
                GET_ALL_CATS_QUEUE,
                "",
                new ParameterizedTypeReference<>() {
                }
        );

        if (!response.getMessage().equals("OK")){
            throw new CatMicroserviceException(response.getMessage());
        }

        return response.getCats();
    }

    void startFriendship(Integer firstId, Integer secondId) throws CatMicroserviceException {
        RabbitListenerCatResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                directExchange.getName(),
                START_FRIENDSHIP_QUEUE,
                List.of(firstId, secondId),
                new ParameterizedTypeReference<>() {
                }
        );

        if (!response.getMessage().equals("OK")){
            throw new CatMicroserviceException(response.getMessage());
        }
    }

    void stopFriendship(Integer firstId, Integer secondId) throws CatMicroserviceException {
        RabbitListenerCatResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                directExchange.getName(),
                STOP_FRIENDSHIP_QUEUE,
                List.of(firstId, secondId),
                new ParameterizedTypeReference<>(){
                }
        );

        if (!response.getMessage().equals("OK")){
            throw new CatMicroserviceException(response.getMessage());
        }
    }

    void deleteCat(Integer catId) throws CatMicroserviceException {
        RabbitListenerCatResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                directExchange.getName(),
                DELETE_CAT_QUEUE,
                catId,
                new ParameterizedTypeReference<>() {
                }
        );

        if (!response.getMessage().equals("OK")){
            throw new CatMicroserviceException(response.getMessage());
        }
    }

    List<CatDto> getByColor(Color color, Integer ownerId) throws CatMicroserviceException {
        List<String> request = ownerId == null ? List.of(color.name()) : List.of(color.name(), ownerId.toString());
        RabbitListenerCatListResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                directExchange.getName(),
                GET_CATS_BY_COLOR_QUEUE,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        if (!response.getMessage().equals("OK")){
            throw new CatMicroserviceException(response.getMessage());
        }


        return response.getCats();
    }

    OwnerDto addOwner(OwnerDto ownerDto) throws OwnerMicroserviceException {
        RabbitListenerOwnerResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                directExchange.getName(),
                ADD_OWNER_QUEUE,
                ownerDto,
                new ParameterizedTypeReference<>() {
                }
        );

        if (!response.getMessage().equals("OK")){
            throw new OwnerMicroserviceException(response.getMessage());
        }

        return response.getOwnerDto();
    }

    void deleteOwner(Integer ownerId) throws OwnerMicroserviceException {
        RabbitListenerOwnerResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                directExchange.getName(),
                DELETE_OWNER_QUEUE,
                ownerId,
                new ParameterizedTypeReference<>() {
                }
        );

        if (!response.getMessage().equals("OK")){
            throw new OwnerMicroserviceException(response.getMessage());
        }
    }
}
