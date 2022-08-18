package com.techprog.upgradedcats.repository;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class OwnerDto {
    private String id = null;
    private String name = null;
    private Calendar dateOfBirth = null;
    private Set<String> catsId = new HashSet<>();
    private String username = null;
    private String password = null;
    private String role = null;

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
