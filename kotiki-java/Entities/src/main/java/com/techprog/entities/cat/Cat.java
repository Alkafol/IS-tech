package com.techprog.entities.cat;

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

    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "breed")
    private String breed;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Id
    @Column(name = "cat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinTable(
            name = "friendship",
            joinColumns = @JoinColumn(name = "first_cat"),
            inverseJoinColumns = @JoinColumn(name = "second_cat")
    )
    private Set<Cat> friends = new HashSet<>();

    public Cat(String name, LocalDate dateOfBirth, String breed, Color color, Integer ownerId){
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.breed = breed;
        this.color = color;
        this.ownerId = ownerId;
    }

    public Cat() {
    }

    @Override
    public int hashCode() {
        int result = 17;
        if (id != null) {
            result = 31 * result + id.hashCode();
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Cat))
            return false;
        Cat other = (Cat)o;
        return this.id != null && this.id.equals(other.id);
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

    public void setOwnerId(Integer ownerId){
        this.ownerId = ownerId;
    }

    public void addFriends(List<Cat> cats){
        friends.addAll(cats);
    }

    public void removeFriend(Cat cat){ friends.remove(cat); }

    public void removeFriendById(Integer catId){
        friends.removeIf(friend -> friend.getId().equals(catId));
    }

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

    public Integer getOwnerId(){
        return ownerId;
    }

    public Integer getId() { return id; }

    public void setId(Integer id){ this.id =  id;}

    public void removeAllFriends(){
        friends.clear();
    }

    @JsonIgnoreProperties("friends ")
    public Set<Cat> getFriends(){ return friends; }
}
