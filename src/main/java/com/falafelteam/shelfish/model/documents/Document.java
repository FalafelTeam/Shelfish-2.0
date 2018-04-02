package com.falafelteam.shelfish.model.documents;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import com.falafelteam.shelfish.model.AuthorKinds.Editor;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.DocumentUser;
import com.falafelteam.shelfish.model.users.User;
import com.falafelteam.shelfish.repository.DocumentUserRepository;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.*;


@Data
@Entity
@Table
@NoArgsConstructor
public class Document {
    //Common
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private boolean isBestseller;
    private int copies;
    private int price;
    private boolean isReference;
    private String tags;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "document", cascade = {CascadeType.MERGE})
    private List<DocumentUser> users;
    @ManyToMany(cascade = CascadeType.DETACH)
    private List<Author> authors;
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "typeId")
    private DocumentType type;
    private boolean hasOutstanding;
    @ManyToOne(fetch = FetchType.EAGER)
    private Publisher publisher;
    @ManyToOne(fetch = FetchType.EAGER)
    private Editor editor;
    private int edition;
    private Date publishingDate;

    // for Book
    public Document(String name, String description, boolean isBestseller, int copies, boolean isReference, List<Author> authors,
                    Publisher publisher, DocumentType type, String tags, Date publishingDate) {
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
        this.tags = tags;
        this.publishingDate = publishingDate;
        this.hasOutstanding = false;
    }

    // for Article
    public Document(String name, String description, boolean isBestseller, int copies, boolean isReference, List<Author> authors,
                    Publisher publisher, Editor editor, DocumentType type, String tags, Date publishingDate) {
        this.name = name;
        this.description = description;
        this.isBestseller = isBestseller;
        this.copies = copies;
        this.isReference = isReference;
        this.type = type;
        this.publisher = publisher;
        this.editor = editor;
        this.users = new LinkedList<>();
        this.tags = tags;
        this.publishingDate = publishingDate;
        this.hasOutstanding = false;
    }

    // for AV
    public Document(String name, String description, boolean isBestselle, int copies, boolean isReference, List<Author> authors,
                    DocumentType type, String tags) {
        this.name = name;
        this.description = description;
        this.isBestseller = isBestseller;
        this.copies = copies;
        this.isReference = isReference;
        this.type = type;
        this.authors = new LinkedList<>();
        this.authors.addAll(authors);
        this.users = new LinkedList<>();
        this.tags = tags;
        this.hasOutstanding = false;
    }

    public LinkedList<String> getTags() {
        LinkedList<String> tagArr = new LinkedList();
        tagArr.addAll(Arrays.asList(tags.split(", ")));
        return tagArr;
    }

    public void setTags(LinkedList<String> tags) {
        for (String tag : tags) {
            this.tags = this.tags + tag;
        }
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public LinkedList<User> deleteNotTakenFromQueue() {
        LinkedList<User> deleted = new LinkedList<>();
        for (DocumentUser toDelete : users) {
            if (toDelete.getStatus() == toDelete.getStatusNEW()) {
                deleted.add(toDelete.getUser());
                users.remove(toDelete);
            }
        }
        return deleted;
    }

    public void addToQueue(DocumentUser docUser) {
        users.add(docUser);
    }

    public void removeFromQueue(DocumentUser docUser) {
        users.remove(docUser);
    }

    @Override
    public String toString() {
        return "";
    }
}
