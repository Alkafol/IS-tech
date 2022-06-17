package com.responseModels;

import java.util.List;

public class CatResponse {
    private String name;
    private String color;
    private String ownerId;
    private String breed;
    private String dateOfBirth;
    private String id;
    private List<String> friendsId;

    public CatResponse(String name, String color, String ownerId, String breed, String dateOfBirth, String id, List<String> friendsId) {
        this.name = name;
        this.color = color;
        this.ownerId = ownerId;
        this.breed = breed;
        this.dateOfBirth = dateOfBirth;
        this.id = id;
        this.friendsId = friendsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getFriendsId() {
        return friendsId;
    }

    public void setFriendsId(List<String> friendsId) {
        this.friendsId = friendsId;
    }
}
