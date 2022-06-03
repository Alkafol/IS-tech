package com.techprog.upgradedcats.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CatDto {
    private String name = null;
    private String id = null;
    private String color = null;
    private String ownerId = null;
    private String breed = null;
    private Calendar dateOfBirth = null;
    private List<String> friendsId = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
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

    public Calendar getDateOfBirth(){
        return dateOfBirth;
    }

    public void setDateOfBirth(Calendar dateOfBirth){
        this.dateOfBirth = dateOfBirth;
    }

    public List<String> getFriendsId() {
        return friendsId;
    }

    public void setFriendsId(List<String> friendsId) {
        this.friendsId = friendsId;
    }
}
