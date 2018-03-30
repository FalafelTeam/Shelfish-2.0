package com.falafelteam.shelfish.model.users;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "User")
public class Student extends Patron {

    public Student(String name, String login, String password, String address, String phoneNumber) {
        super(name, login, password, address, phoneNumber, 0);
    }
}
