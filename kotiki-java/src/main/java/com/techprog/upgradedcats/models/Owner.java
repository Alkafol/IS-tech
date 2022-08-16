package com.techprog.upgradedcats.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "owner", schema = "public")
public class Owner implements Serializable {
    @Id
    @Column(name = "id")
    private String _id = UUID.randomUUID().toString();

    @Column(name = "name")
    private String _name;

    @Column(name = "date_of_birth")
    private Calendar _dateOfBirth;

    @OneToMany(mappedBy = "_owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Cat> cats = new HashSet<>();

    public Owner(String name, Calendar dateOfBirth){
        _name = name;
        _dateOfBirth = dateOfBirth;
    }

    public Owner() {
    }

    public static Owner createCustomOwner(String name, String id, Calendar dateOfBirth, Set<Cat> cats){
        Owner owner = new Owner(name, dateOfBirth);
        if (id == null){
            return owner;
        }
        owner.setId(id);
        owner.setCats(cats);
        return owner;
    }

    public void setName(String name){
        _name = name;
    }

    public void setDateOfBirth(Calendar dateOfBirth){
        _dateOfBirth = dateOfBirth;
    }

    public void setCats(Set<Cat> newCats){
        cats = newCats;
    }

    public String getName(){
        return _name;
    }

    public Calendar getDateOfBirth(){
        return _dateOfBirth;
    }

    public String getId() { return _id; }

    public Set<Cat> getCats(){ return cats; }

    public void addCat(Cat cat){
        cats.add(cat);
    }

    public void removeCat(Cat cat){
        cats.remove(cat);
    }

    private void setId(String id){ _id = id; }
}

