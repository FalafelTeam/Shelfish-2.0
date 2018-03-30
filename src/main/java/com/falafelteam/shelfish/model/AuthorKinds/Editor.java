package com.falafelteam.shelfish.model.AuthorKinds;

import javax.persistence.Entity;

@Entity
public class Editor extends AuthorsKinds {
    public Editor(String name) {
        super(name);
    }
}
