package com.techprog.upgradedcats.dto;

import com.techprog.upgradedcats.repository.CatRepository;
import com.techprog.upgradedcats.repository.OwnerRepository;
import com.techprog.upgradedcats.models.Cat;
import com.techprog.upgradedcats.models.Color;
import com.techprog.upgradedcats.models.Owner;
import com.techprog.upgradedcats.models.User;
import com.techprog.upgradedcats.security.ApplicationUserRole;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Mapper {
    CatRepository catRepository;
    OwnerRepository ownerRepository;

    Mapper(CatRepository catRepository, OwnerRepository ownerRepository){
        this.catRepository = catRepository;
        this.ownerRepository = ownerRepository;
    }

    public CatDto convertToCatDto(Cat cat){
        String name = cat.getName();
        String breed = cat.getBreed();
        String id = cat.getId();
        String dateOfBirth = cat.getDateOfBirth().toString();
        String ownerId = null;

        if (cat.getOwner() != null) {
            ownerId = cat.getOwner().getId();
        }

        List<String> friendsId = cat.getFriends().stream().map(Cat::getId).toList();
        String color = cat.getColor().name();
        return new CatDto(name, id, color, ownerId, breed, dateOfBirth, friendsId);
    }

    public OwnerDto convertToOwnerDto(Owner owner){
        String name = owner.getName();
        String id = owner.getId();
        String dateOfBirth = owner.getDateOfBirth().toString();
        List<String> catsId = owner.getCats().stream().map(Cat::getId).collect(Collectors.toList());
        return new OwnerDto(id, name, dateOfBirth, catsId);
    }

    public Cat convertToCat(CatDto catDto) {
        String name = catDto.getName();
        String breed = catDto.getBreed();
        String id = catDto.getId();
        Color color = Color.valueOf(catDto.getColor());

        Owner owner = null;
        if (catDto.getOwnerId() != null) {
            owner = ownerRepository.getById(catDto.getOwnerId());
        }

        LocalDate dateOfBirth = LocalDate.parse(catDto.getDateOfBirth());
        Set<Cat> friends = new HashSet<>();
        if (catDto.getFriendsId() != null) {
            friends = catDto.getFriendsId().stream().map(friendId -> catRepository.findById(friendId).get()).collect(Collectors.toSet());
        }

        return Cat.createCustomCat(name, id, dateOfBirth, breed, color, owner, friends);
    }

    public Owner convertToOwner(OwnerDto ownerDto){
        String name = ownerDto.getName();
        LocalDate dateOfBirth = LocalDate.parse(ownerDto.getDateOfBirth());
        String id = ownerDto.getId();
        Set<Cat> cats = new HashSet<>();
        if (ownerDto.getCatsId() != null) {
            cats = ownerDto.getCatsId().stream().map(catId -> catRepository.findById(catId).get()).collect(Collectors.toSet());
        }

        return Owner.createCustomOwner(name, id, dateOfBirth, cats);
    }

    public UserDto convertToUserDto(User user){
        String username = user.getUsername();
        String password = user.getPassword();
        String role = user.getRole().name();

        String ownerName = user.getOwner().getName();
        String ownerId = user.getOwner().getId();
        String ownerDateOfBirth = user.getOwner().getDateOfBirth().toString();
        List<String> catsId = user.getOwner().getCats().stream().map(Cat::getId).toList();

        return new UserDto(username, password, role, ownerName, ownerId, ownerDateOfBirth, catsId);
    }

    public User convertToUser(UserDto userDto){
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        ApplicationUserRole role = ApplicationUserRole.valueOf(userDto.getRole());

        String ownerName = userDto.getOwnerName();
        String ownerId = userDto.getOwnerId();
        String ownerDateOfBirth = userDto.getOwnerDateOfBirth();
        List<String> catsId = userDto.getCatsId();

        Owner owner = convertToOwner(new OwnerDto(ownerId, ownerName, ownerDateOfBirth, catsId));

        return  new User(username, password, role, owner);
    }



}
