package com.falafelteam.shelfish.model.documents;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import com.falafelteam.shelfish.model.AuthorKinds.Editor;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.DocumentUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import javax.validation.constraints.*;


@Data
@Entity
@Table
@NoArgsConstructor
public class Document {
    //Common
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String description;
    private boolean isBestseller;
    private int copies;
    private int price;
    private Boolean isReference;
    private List<String> tags;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "document", cascade = {CascadeType.MERGE})
    private List<DocumentUser> users;
    @ManyToMany(cascade = CascadeType.DETACH)
    @NotEmpty
    private List<Author> authors;
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "typeId")
    private DocumentType type;

    //Book
    @ManyToOne(fetch = FetchType.EAGER)
    @NotEmpty
    private Publisher publisher;

    //Article
    @ManyToOne(fetch = FetchType.EAGER)
    @NotEmpty
    private Editor editor;

    // for Book (Multiple Authors)
    public Document(String name, String description, boolean isBestseller, int copies, boolean isReference, List<Author> authors,
                    Publisher publisher, DocumentType type, List<String> tags) {
        this.name = name;
        this.description = description;
        this.isBestseller = isBestseller;
        this.copies = copies;
        this.isReference = isReference;
        this.type = type;
        this.publisher = publisher;
        this.authors = new LinkedList<>();
        this.authors.addAll(authors);
        this.users = new LinkedList<>();
        this.tags = new LinkedList<>();
        this.tags.addAll(tags);
    }

    // for Article
    public Document(String name, String description, boolean isBestseller, int copies, boolean isReference, Publisher publisher,
                    Editor editor, DocumentType type, List<String> tags) {
        this.name = name;
        this.description = description;
        this.isBestseller = isBestseller;
        this.copies = copies;
        this.isReference = isReference;
        this.type = type;
        this.publisher = publisher;
        this.editor = editor;
        this.users = new LinkedList<>();
        this.tags = new LinkedList<>();
        this.tags.addAll(tags);
    }

    // for AV
    public Document(String name, String description, boolean isBestseller, int copies, boolean isReference, Author author,
                    DocumentType type, List<String> tags) {
        this.name = name;
        this.description = description;
        this.isBestseller = isBestseller;
        this.copies = copies;
        this.isReference = isReference;
        this.type = type;
        this.authors = new LinkedList<>();
        this.authors.add(author);
        this.users = new LinkedList<>();
        this.tags = new LinkedList<>();
        this.tags.addAll(tags);
    }

}
