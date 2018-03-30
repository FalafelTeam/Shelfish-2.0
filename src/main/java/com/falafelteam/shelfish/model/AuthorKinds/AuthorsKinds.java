package com.falafelteam.shelfish.model.AuthorKinds;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public abstract class AuthorsKinds {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;

    public AuthorsKinds(String name) {
        this.name = name;
    }
}
