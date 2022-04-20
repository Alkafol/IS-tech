package Models;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "cat", schema = "public")
public class Cat implements Serializable {

    @Column(name = "cat_name")
    private String _name;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private Color _color;

    @ManyToOne
    @JoinTable(
            name = "ownership",
            joinColumns = @JoinColumn(name = "cat_id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id")
    )
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

    public Set<Cat> getFriends(){ return _friends; }
}
