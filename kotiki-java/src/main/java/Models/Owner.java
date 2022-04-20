package Models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "owner", schema = "public")
public class Owner implements Serializable{
    @Id
    @Column(name = "id")
    private String _id = UUID.randomUUID().toString();

    @Column(name = "name")
    private String _name;

    @Column(name = "date_of_birth")
    private Calendar _dateOfBirth;

    @OneToMany(mappedBy = "_owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Cat> cats = new HashSet<>();

    public Owner(String name, Calendar dateOfBirth){
        _name = name;
        _dateOfBirth = dateOfBirth;
    }

    public Owner() {
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
        System.out.println(cats);
    }
}
