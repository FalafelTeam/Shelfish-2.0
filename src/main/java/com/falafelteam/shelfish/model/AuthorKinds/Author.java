package com.falafelteam.shelfish.model.AuthorKinds;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter private Integer id;
    @Getter @Setter
    private String name;

    public Author(String name) {
        this.name = name;
    }

//    @Override
//    public String toString(){
//        return "";
//    }
}
