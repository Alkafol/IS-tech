package com.techprog.upgradedcats.models;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "cat", schema = "public")
public class Cat implements Serializable {

    @Column(name = "cat_name")
    private String _name;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private Color _color;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ownership",
            joinColumns = @JoinColumn(name = "cat_id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id")
    )
    @JsonIgnore
    private Owner _owner;

    @Column(name = "breed")
    private String _breed;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Calendar _dateOfBirth;

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
    private Set<Cat> _friends = new HashSet<>();

    public Cat(String name, Calendar dateOfBirth, String breed, Color color, Owner owner){
        _name = name;
        _dateOfBirth = dateOfBirth;
        _breed = breed;
        _color = color;
        _owner = owner;
        id = UUID.randomUUID().toString();
    }

    public Cat() {
    }

    public static Cat createCustomCat(String name, String id, Calendar dateOfBirth, String breed, Color color, Owner owner, Set<Cat> friends){
        Cat cat = new Cat(name, dateOfBirth, breed, color, owner);
        if (id == null){
            return cat;
        }
        cat.setId(id);
        cat.setFriends(friends);
        return cat;
    }

    public void setName(String name){
        _name = name;
    }

    public void setDateOfBirth(Calendar dateOfBirth){
        _dateOfBirth = dateOfBirth;
    }

    public void setBreed(String breed){
        _breed = breed;
    }

    public void setColor(Color color){
        _color = color;
    }

    public void setFriends(Set<Cat> friends){
        _friends = friends;
    }

    public void setOwner(Owner owner){
        _owner = owner;
    }

    public void addFriend(Cat cat){
        _friends.add(cat);
    }

    public void removeFriend(Cat cat){ _friends.remove(cat); }

    public String getName(){
        return _name;
    }

    public Calendar getDateOfBirth(){
        return _dateOfBirth;
    }

    public String getBreed(){
        return _breed;
    }

    public Color getColor(){
        return _color;
    }

    public Owner getOwner(){
        return _owner;
    }

    public String getId() { return id; }

    private void setId(String id){ this.id =  id;}

    public void removeAllFriends(){
        _friends.clear();
    }

    @JsonIgnoreProperties("friends ")
    public Set<Cat> getFriends(){ return _friends; }
}
