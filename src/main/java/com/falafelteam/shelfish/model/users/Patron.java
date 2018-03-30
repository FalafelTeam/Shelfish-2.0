package com.falafelteam.shelfish.model.users;

import com.falafelteam.shelfish.model.DocumentUser;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

@Data
public abstract class Patron extends User {
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = {CascadeType.ALL})
    private List<DocumentUser> documents;
    private int priority;

    public Patron(String name, String login, String password, String address, String phoneNumber, int priority) {
        super(name, login, password, address, phoneNumber);
        documents = new LinkedList<DocumentUser>();
        this.priority = priority;
    }
}
