package com.techprog.upgradedcats.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private String name;

    @Column(name = "date_of_birth")
    private Calendar dateOfBirth;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Cat> cats = new HashSet<>();

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "username")
    private User user;

    public Owner(String name, Calendar dateOfBirth, User user){
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.user = user;
    }

    public Owner() {
    }

    public static Owner createCustomOwner(String name, String id, Calendar dateOfBirth, Set<Cat> cats, User user){
        Owner owner = new Owner(name, dateOfBirth, user);
        if (id == null){
            return owner;
        }
        owner.setId(id);
        owner.setCats(cats);
        return owner;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDateOfBirth(Calendar dateOfBirth){
        this.dateOfBirth = dateOfBirth;
    }

    public void setCats(Set<Cat> newCats){
        cats = newCats;
    }

    public String getName(){
        return name;
    }

    public Calendar getDateOfBirth(){
        return dateOfBirth;
    }

    public String getId() { return _id; }

    public Set<Cat> getCats(){ return cats; }

    public void addCat(Cat cat){
        cats.add(cat);
    }

    public void removeCat(Cat cat){
        cats.remove(cat);
    }

    public User getUser(){ return user; }

    private void setId(String id){ _id = id; }
}

