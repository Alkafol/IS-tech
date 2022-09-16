package com.techprog.upgradedcats.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
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
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Cat> cats = new HashSet<>();

    public Owner(String name, LocalDate dateOfBirth){
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public Owner() {
    }

    public static Owner createCustomOwner(String name, String id, LocalDate dateOfBirth, Set<Cat> cats){
        Owner owner = new Owner(name, dateOfBirth);
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

    public void setDateOfBirth(LocalDate dateOfBirth){
        this.dateOfBirth = dateOfBirth;
    }

    public void setCats(Set<Cat> newCats){
        cats = newCats;
    }

    public String getName(){
        return name;
    }

    public LocalDate getDateOfBirth(){
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

    private void setId(String id){ _id = id; }
}

