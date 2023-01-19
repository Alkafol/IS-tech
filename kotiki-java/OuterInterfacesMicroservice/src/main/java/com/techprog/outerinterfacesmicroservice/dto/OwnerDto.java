package com.techprog.outerinterfacesmicroservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class OwnerDto {
    private String id;
    private String name;
    private String dateOfBirth;
    private List<String> catsId;

    public OwnerDto(){}

    public OwnerDto(String id, String name, String dateOfBirth, List<String> catsId) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.catsId = catsId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public List<String> getCatsId() {
        return catsId;
    }
}
