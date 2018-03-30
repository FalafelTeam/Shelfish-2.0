package com.falafelteam.shelfish.model.AuthorKinds;

import javax.persistence.Entity;

@Entity
public class Author extends AuthorsKinds {
    public Author(String name) {
        super(name);
    }
}
