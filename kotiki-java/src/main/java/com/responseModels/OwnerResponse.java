package com.responseModels;

import java.util.List;

public class OwnerResponse {
    private String id;
    private String name;
    private String dateOfBirth;

    public OwnerResponse(String id, String name, String dateOfBirth, List<String> catsId) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.catsId = catsId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<String> getCatsId() {
        return catsId;
    }

    public void setCatsId(List<String> catsId) {
        this.catsId = catsId;
    }

    private List<String> catsId;
}
