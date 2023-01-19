package com.techprog.entities.owner;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "owner", schema = "public")
public class Owner implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    @Column(name = "name")
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name = "ownership")
    private Set<Integer> catsId = new HashSet<>();

    public Owner(String name, LocalDate dateOfBirth){
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public Owner() {
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDateOfBirth(LocalDate dateOfBirth){
        this.dateOfBirth = dateOfBirth;
    }

    public void setCatsId(Set<Integer> catsId){
        this.catsId = catsId;
    }

    public String getName(){
        return name;
    }

    public LocalDate getDateOfBirth(){
        return dateOfBirth;
    }

    public Integer getId() { return id; }

    public Set<Integer> getCatsId(){ return catsId; }

    public void addCat(Integer catId){
        catsId.add(catId);
    }

    public void removeCat(Integer catId){
        catsId.remove(catId);
    }

    public void setId(Integer id){ this.id = id; }
}

