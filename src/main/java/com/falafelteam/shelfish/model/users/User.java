package com.falafelteam.shelfish.model.users;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "userDiscriminatorColumn")
@Table(name = "User")
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String login;
    private String password;
    private String address;
    private String phoneNumber;

    public User(String name, String login, String password, String address, String phoneNumber) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
