package com.techprog.upgradedcats.dto;

import java.util.List;

public class CatDto {
    private final String name;
    private final String id;
    private final String color;
    private final String ownerId;
    private final String breed;
    private final String dateOfBirth;
    private final List<String> friendsId;

    public CatDto(String name, String id, String color, String ownerId, String breed, String dateOfBirth, List<String> friendsId) {
        this.name = name;
        this.id = id;
        this.color = color;
        this.ownerId = ownerId;
        this.breed = breed;
        this.dateOfBirth = dateOfBirth;
        this.friendsId = friendsId;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getBreed() {
        return breed;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public List<String> getFriendsId() {
        return friendsId;
    }
}
