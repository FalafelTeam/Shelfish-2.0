package com.falafelteam.shelfish.model.documents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class DocumentType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    String name;

    public DocumentType(String name) {
        this.name = name;
    }

//    @Override
//    public String toString(){
//        return "";
//    }
}
