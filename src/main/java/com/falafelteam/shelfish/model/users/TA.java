package com.falafelteam.shelfish.model.users;

import javax.persistence.Table;

@Table(name = "User")
public class TA extends Patron {
    public TA(String name, String login, String password, String address, String phoneNumber) {
        super(name, login, password, address, phoneNumber, 2);
    }
}
