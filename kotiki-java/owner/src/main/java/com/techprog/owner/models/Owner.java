package com.techprog.owner.models;

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

<<<<<<< Updated upstream:kotiki-java/src/main/java/com/techprog/upgradedcats/models/Owner.java
    @OneToMany(mappedBy = "_owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Cat> cats = new HashSet<>();

    public Owner(String name, Calendar dateOfBirth){
        _name = name;
        _dateOfBirth = dateOfBirth;
=======
    @Column(name = "cats_id")
    @ElementCollection
    private Set<String> catsId = new HashSet<>();

    @Column(name = "username")
    private String username;

    public Owner(String name, Calendar dateOfBirth, String username){
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
>>>>>>> Stashed changes:kotiki-java/owner/src/main/java/com/techprog/owner/models/Owner.java
    }

    public Owner() {
    }

<<<<<<< Updated upstream:kotiki-java/src/main/java/com/techprog/upgradedcats/models/Owner.java
    public static Owner createCustomOwner(String name, String id, Calendar dateOfBirth, Set<Cat> cats){
        Owner owner = new Owner(name, dateOfBirth);
=======
    public static Owner createCustomOwner(String name, String id, Calendar dateOfBirth, Set<String> catsId, String username){
        Owner owner = new Owner(name, dateOfBirth, username);
>>>>>>> Stashed changes:kotiki-java/owner/src/main/java/com/techprog/owner/models/Owner.java
        if (id == null){
            return owner;
        }
        owner.setId(id);
        owner.setCatsId(catsId);
        return owner;
    }

    public void setName(String name){
        _name = name;
    }

    public void setDateOfBirth(Calendar dateOfBirth){
        _dateOfBirth = dateOfBirth;
    }

    public void setCatsId(Set<String> catsId){
        this.catsId = catsId;
    }

    public String getName(){
        return _name;
    }

    public Calendar getDateOfBirth(){
        return _dateOfBirth;
    }

    public String getId() { return _id; }

    public Set<String > getCatsId(){ return catsId; }

    public void addCat(String catId){
        catsId.add(catId);
    }

    public void removeCat(String catId){
        catsId.remove(catId);
    }

<<<<<<< Updated upstream:kotiki-java/src/main/java/com/techprog/upgradedcats/models/Owner.java
=======
    public String getUsername(){ return username; }

>>>>>>> Stashed changes:kotiki-java/owner/src/main/java/com/techprog/owner/models/Owner.java
    private void setId(String id){ _id = id; }
}

