package com.techprog.upgradedcats.dto;

import java.util.List;

public class OwnerDto {
    private final String id;
    private final String name;
    private final String dateOfBirth;
    private final List<String> catsId;


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
