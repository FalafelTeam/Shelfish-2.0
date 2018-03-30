package com.falafelteam.shelfish.model.users;

import javax.persistence.Table;

@Table(name = "User")
public class VisitingProfessor extends Patron {
    public VisitingProfessor(String name, String login, String password, String address, String phoneNumber) {
        super(name, login, password, address, phoneNumber, 3);
    }
}
