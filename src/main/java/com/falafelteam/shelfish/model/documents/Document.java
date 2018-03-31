package com.falafelteam.shelfish.model.documents;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import com.falafelteam.shelfish.model.AuthorKinds.Editor;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.DocumentUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    //Common
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String description;
    private boolean isBestseller;
    private Integer copies;
    private Integer price;
    private Boolean isReference;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "document", cascade = {CascadeType.MERGE})
    private List<DocumentUser> users;
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<Author> authors;
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "typeId")
    private DocumentType type;

    //Book
    @ManyToOne(fetch = FetchType.EAGER)
    private Publisher publisher;

    //Article
    @ManyToOne(fetch = FetchType.EAGER)
    private Editor editor;
}
