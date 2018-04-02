package com.falafelteam.shelfish.model.documents;

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
public class DocumentType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter Integer id;
    @Getter @Setter String name;

    public DocumentType(String name) {
        this.name = name;
    }

//    @Override
//    public String toString(){
//        return "";
//    }
}
