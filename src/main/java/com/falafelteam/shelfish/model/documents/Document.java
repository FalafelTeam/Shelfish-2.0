package com.falafelteam.shelfish.model.documents;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import com.falafelteam.shelfish.model.AuthorKinds.Editor;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.DocumentUser;
import com.falafelteam.shelfish.model.users.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
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
    private boolean isReference;
    private String tags;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "document", cascade = {CascadeType.MERGE})
    private List<DocumentUser> users;
    @ManyToMany(cascade = CascadeType.DETACH)
    @NotEmpty
    private List<Author> authors;
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "typeId")
    private DocumentType type;
    private boolean hasOutstanding;

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
                    Publisher publisher, DocumentType type, String tags) {
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
        this.hasOutstanding = false;
    }

    // for Article
    public Document(String name, String description, boolean isBestseller, int copies, boolean isReference, Publisher publisher,
                    Editor editor, DocumentType type, String tags) {
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
        this.hasOutstanding = false;
    }

    // for AV
    public Document(String name, String description, boolean isBestseller, int copies, boolean isReference, Author author,
                    DocumentType type, String tags) {
        this.name = name;
        this.description = description;
        this.isBestseller = isBestseller;
        this.copies = copies;
        this.isReference = isReference;
        this.type = type;
        this.authors = new LinkedList<>();
        this.authors.add(author);
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

    public LinkedList<User> deleteNotTakenFromQueue(){
        LinkedList<User> deleted = new LinkedList<>();
        for(DocumentUser toDelete : users){
            if(toDelete.getStatus() == toDelete.getStatusNEW()){
                deleted.add(toDelete.getUser());
                users.remove(toDelete);
            }
        }
        return deleted;
    }
}
