package com.falafelteam.shelfish.model.users;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotEmpty
    private String name;
    @Email
    private String login;
    private String password;
    @NotEmpty
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
