package com.techprog.outerinterfacesmicroservice.dto;

import java.util.List;

public class UserInfoDto {
    private String username;
    private String role;
    private String ownerId;
    private String ownerName;
    private String ownerDateOfBirth;
    private List<String> catsId;

    public UserInfoDto(String username, String role, String ownerId, String ownerName, String ownerDateOfBirth, List<String> catsId) {
        this.username = username;
        this.role = role;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.ownerDateOfBirth = ownerDateOfBirth;
        this.catsId = catsId;
    }

    public String getUsername() {
        return username;
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

    public List<String> getCatsId() {
        return catsId;
    }
}
