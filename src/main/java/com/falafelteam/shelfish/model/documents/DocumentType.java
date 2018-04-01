package com.falafelteam.shelfish.model.documents;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class DocumentType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    String type;

    public DocumentType(String type) {
        this.type = type;
    }
}
