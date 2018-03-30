package com.falafelteam.shelfish.model.users;

import javax.persistence.Table;

@Table(name = "User")
public class Professor extends Patron {
    public Professor(String name, String login, String password, String address, String phoneNumber) {
        super(name, login, password, address, phoneNumber, 4);
    }
}
