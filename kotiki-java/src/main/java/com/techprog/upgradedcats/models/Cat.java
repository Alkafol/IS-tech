package com.techprog.upgradedcats.models;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "cat", schema = "public")
public class Cat implements Serializable {

    @Column(name = "cat_name")
    private String name;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private Color color;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "ownership",
            joinColumns = @JoinColumn(name = "cat_id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id")
    )
    @JsonIgnore
    private Owner owner;

    @Column(name = "breed")
    private String breed;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Id
    @Column(name = "cat_id")
    private String id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinTable(
            name = "friendship",
            joinColumns = @JoinColumn(name = "first_cat"),
            inverseJoinColumns = @JoinColumn(name = "second_cat")
    )
    private Set<Cat> friends = new HashSet<>();

    public Cat(String name, LocalDate dateOfBirth, String breed, Color color, Owner owner){
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.breed = breed;
        this.color = color;
        this.owner = owner;
        id = UUID.randomUUID().toString();
    }

    public Cat() {
    }

    public static Cat createCustomCat(String name, String id, LocalDate dateOfBirth, String breed, Color color, Owner owner, Set<Cat> friends){
        Cat cat = new Cat(name, dateOfBirth, breed, color, owner);
        if (id == null){
            return cat;
        }
        cat.setId(id);
        cat.setFriends(friends);
        return cat;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDateOfBirth(LocalDate dateOfBirth){
        this.dateOfBirth = dateOfBirth;
    }

    public void setBreed(String breed){
        this.breed = breed;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public void setFriends(Set<Cat> friends){
        this.friends = friends;
    }

    public void setOwner(Owner owner){
        this.owner = owner;
    }

    public void addFriend(Cat cat){
        friends.add(cat);
    }

    public void removeFriend(Cat cat){ friends.remove(cat); }

    public String getName(){
        return name;
    }

    public LocalDate getDateOfBirth(){
        return dateOfBirth;
    }

    public String getBreed(){
        return breed;
    }

    public Color getColor(){
        return color;
    }

    public Owner getOwner(){
        return owner;
    }

    public String getId() { return id; }

    private void setId(String id){ this.id =  id;}

    public void removeAllFriends(){
        friends.clear();
    }

    @JsonIgnoreProperties("friends ")
    public Set<Cat> getFriends(){ return friends; }
}
