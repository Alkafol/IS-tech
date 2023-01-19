package com.techprog.outerinterfacesmicroservice.dto;

public class UserCreationDto {
    private final String username;
    private final String password;
    private final String role;
    private final String ownerId;
    private final String ownerName;
    private final String ownerDateOfBirth;

    public UserCreationDto(String username, String password, String role, String ownerId, String ownerName, String ownerDateOfBirth) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.ownerDateOfBirth = ownerDateOfBirth;
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

    public String getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerDateOfBirth() {
        return ownerDateOfBirth;
    }
}
