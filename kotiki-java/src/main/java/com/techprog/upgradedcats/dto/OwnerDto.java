package com.techprog.upgradedcats.dto;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OwnerDto {
    private String id = null;
    private String name = null;
    private Calendar dateOfBirth = null;
    private Set<String> catsId = new HashSet<>();

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

    public Calendar getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Calendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<String> getCatsId() {
        return catsId;
    }

    public void setCatsId(Set<String> catsId) {
        this.catsId = catsId;
    }
}
