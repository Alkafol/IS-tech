package com.techprog.outerInterface.models;

import com.techprog.outerInterface.security.ApplicationUserRole;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user", schema = "public")
public class User implements Serializable {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ApplicationUserRole role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String _username) {
        this.username = _username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String _password) {
        this.password = _password;
    }

    public ApplicationUserRole getRole() {
        return role;
    }

    public void setRole(ApplicationUserRole role) {
        this.role = role;
    }

    public User(String username, String password, ApplicationUserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(){
    }
}