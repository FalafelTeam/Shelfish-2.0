package com.falafelteam.shelfish.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Class for the user model
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String login;
    private String password;
    private String address;
    private String phoneNumber;
    @OneToOne(cascade = CascadeType.DETACH)
    private Role role;

    public User(String name, String login, String password, String address, String phoneNumber, Role role) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
