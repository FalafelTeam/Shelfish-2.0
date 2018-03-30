package com.falafelteam.shelfish.model.documents;

import com.falafelteam.shelfish.model.DocumentUser;
import lombok.Data;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "documentDiscriminatorColumn")
@Table(name = "Document")
public abstract class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String description;
    private boolean isBestseller;
    private Integer copies;
    private Integer price;
    private Boolean isReference;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "document", cascade = {CascadeType.ALL})
    private List<DocumentUser> users;

    public Document(String name, boolean isBestseller, int copies, int price, boolean isReference) {
        this.name = name;
        this.isBestseller = isBestseller;
        this.copies = copies;
        this.price = price;
        this.isReference = isReference;
        users = new LinkedList<DocumentUser>();
        this.description = "";
    }
}
