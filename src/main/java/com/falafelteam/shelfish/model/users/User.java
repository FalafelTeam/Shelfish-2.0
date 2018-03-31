package com.falafelteam.shelfish.model.users;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; // in all args consrtuctor put 0 (-1 or whatever)
    private String name;
    private String login;
    private String password;
    private String address;
    private String phoneNumber;
    @OneToOne(cascade = CascadeType.DETACH)
    private Role role;
}
