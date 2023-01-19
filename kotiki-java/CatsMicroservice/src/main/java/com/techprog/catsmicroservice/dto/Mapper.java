package com.techprog.catsmicroservice.dto;

import com.techprog.catsmicroservice.repository.CatRepository;
import com.techprog.entities.cat.Cat;
import com.techprog.entities.cat.Color;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Mapper {
    CatRepository catRepository;

    Mapper(CatRepository catRepository){
        this.catRepository = catRepository;
    }

    public CatDto convertToCatDto(Cat cat){
        String name = cat.getName();
        String breed = cat.getBreed();
        String id = cat.getId().toString();
        String dateOfBirth = cat.getDateOfBirth().toString();
        String ownerId = null;

        if (cat.getOwnerId() != null) {
            ownerId = cat.getOwnerId().toString();
        }

        List<String> friendsId = cat.getFriends().stream().map(Cat::getId).map(String::valueOf).toList();
        String color = cat.getColor().name();
        return new CatDto(name, id, color, ownerId, breed, dateOfBirth, friendsId);
    }

    public Cat convertToCat(CatDto catDto) {
        String name = catDto.getName();
        String breed = catDto.getBreed();
        Integer id = catDto.getId() == null ? null : Integer.valueOf(catDto.getId());
        Color color = Color.valueOf(catDto.getColor());
        Integer ownerId = Integer.parseInt(catDto.getOwnerId());

        LocalDate dateOfBirth = LocalDate.parse(catDto.getDateOfBirth());
        Set<Cat> friends = new HashSet<>();
        if (catDto.getFriendsId() != null) {
            friends = catDto.getFriendsId().stream().map(friendId -> catRepository.findById(Integer.parseInt(friendId)).get()).collect(Collectors.toSet());
        }

        Cat cat = new Cat(name, dateOfBirth, breed, color, ownerId);
        cat.setFriends(friends);
        if (id != null) {
            cat.setId(id);
        }
        return cat;
    }

    public RabbitListenerCatResponse convertToRabbitListenerResponse(String message, CatDto catDto){
        return new RabbitListenerCatResponse(message, catDto);
    }

    public RabbitListenerCatListResponse convertToRabbitListenerListResponse(String message, List<CatDto> catDtoList){
        return new RabbitListenerCatListResponse(message, catDtoList);
    }
}
