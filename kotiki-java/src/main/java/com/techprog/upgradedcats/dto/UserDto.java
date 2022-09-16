package com.techprog.upgradedcats.dto;

import java.util.List;

public class UserDto {
    private final String username;
    private final String password;
    private final String role;
    private final String ownerName;
    private final String ownerId;
    private final String ownerDateOfBirth;
    private final List<String> catsId;

    public UserDto(String username, String password, String role, String ownerName, String ownerId,
                   String ownerDateOfBirth, List<String> catsId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.ownerName = ownerName;
        this.ownerId = ownerId;
        this.ownerDateOfBirth = ownerDateOfBirth;
        this.catsId = catsId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getOwnerDateOfBirth() {
        return ownerDateOfBirth;
    }

    public List<String> getCatsId() {
        return catsId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
