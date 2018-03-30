package com.falafelteam.shelfish.model.AuthorKinds;

import javax.persistence.Entity;

@Entity
public class Publisher extends AuthorsKinds {
    public Publisher(String name) {
        super(name);
    }
}
