package com.techprog.cat.models;

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

<<<<<<< Updated upstream:kotiki-java/src/main/java/com/techprog/upgradedcats/models/Cat.java
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ownership",
            joinColumns = @JoinColumn(name = "cat_id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id")
    )
    @JsonIgnore
    private Owner _owner;
=======
    @Column(name = "owner_id")
    private String ownerId;
>>>>>>> Stashed changes:kotiki-java/cat/src/main/java/com/techprog/cat/models/Cat.java

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

<<<<<<< Updated upstream:kotiki-java/src/main/java/com/techprog/upgradedcats/models/Cat.java
    public Cat(String name, Calendar dateOfBirth, String breed, Color color, Owner owner){
        _name = name;
        _dateOfBirth = dateOfBirth;
        _breed = breed;
        _color = color;
        _owner = owner;
=======
    public Cat(String name, Calendar dateOfBirth, String breed, Color color, String ownerId){
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.breed = breed;
        this.color = color;
        this.ownerId = ownerId;
>>>>>>> Stashed changes:kotiki-java/cat/src/main/java/com/techprog/cat/models/Cat.java
        id = UUID.randomUUID().toString();
    }

    public Cat() {
    }

    public static Cat createCustomCat(String name, String id, Calendar dateOfBirth, String breed, Color color, String ownerId, Set<Cat> friends){
        Cat cat = new Cat(name, dateOfBirth, breed, color, ownerId);
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

<<<<<<< Updated upstream:kotiki-java/src/main/java/com/techprog/upgradedcats/models/Cat.java
    public void setOwner(Owner owner){
        _owner = owner;
=======
    public void setOwnerId(String ownerId){
        this.ownerId = ownerId;
>>>>>>> Stashed changes:kotiki-java/cat/src/main/java/com/techprog/cat/models/Cat.java
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

<<<<<<< Updated upstream:kotiki-java/src/main/java/com/techprog/upgradedcats/models/Cat.java
    public Owner getOwner(){
        return _owner;
=======
    public String getOwnerId(){
        return ownerId;
>>>>>>> Stashed changes:kotiki-java/cat/src/main/java/com/techprog/cat/models/Cat.java
    }

    public String getId() { return id; }

    private void setId(String id){ this.id =  id;}

    public void removeAllFriends(){
        _friends.clear();
    }

    @JsonIgnoreProperties("friends ")
    public Set<Cat> getFriends(){ return _friends; }
}
