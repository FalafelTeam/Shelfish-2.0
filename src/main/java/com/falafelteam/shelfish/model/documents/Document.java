package com.falafelteam.shelfish.model.documents;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import com.falafelteam.shelfish.model.AuthorKinds.Editor;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.DocumentUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

/**
 * Class for the document model
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Document {

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
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "document", cascade = {CascadeType.ALL})
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

    /**
     * constructor for the document of type "Article"
     */
    public Document(String name, String description, boolean isBestseller, int copies, int price, boolean isReference, List<Author> authors,
                    Publisher publisher, Editor editor, DocumentType type, String tags, Date publishingDate) {
        this.name = name;
        this.description = description;
        this.isBestseller = isBestseller;
        this.copies = copies;
        this.price = price;
        this.isReference = isReference;
        this.type = type;
        this.publisher = publisher;
        this.editor = editor;
        this.users = new LinkedList<>();
        this.tags = tags;
        this.publishingDate = publishingDate;
        this.hasOutstanding = false;
    }

    /**
     * constructor for the document of type "Audio/Video Material"
     */
    public Document(String name, String description, int copies, int price, boolean isReference, List<Author> authors,
                    DocumentType type, String tags) {
        this.name = name;
        this.description = description;
        this.isBestseller = false;
        this.copies = copies;
        this.price = price;
        this.isReference = isReference;
        this.type = type;
        this.authors = new LinkedList<>();
        this.authors.addAll(authors);
        this.users = new LinkedList<>();
        this.tags = tags;
        this.hasOutstanding = false;
    }

    /**
     * constructor for the document of type "Book"
     */
    public Document(String name, String description, boolean isBestseller, int copies, int price, boolean isReference, List<Author> authors,
                    Publisher publisher, DocumentType type, String tags, Date publishingDate) {
        this.name = name;
        this.description = description;
        this.isBestseller = isBestseller;
        this.copies = copies;
        this.price = price;
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

    /**
     * method that returns tags of the document as a list of tags
     *
     * @return list of tags
     */
    public LinkedList<String> getTags() {
        LinkedList<String> tagArr = new LinkedList();
        tagArr.addAll(Arrays.asList(tags.split(", ")));
        return tagArr;
    }

    /**
     * method that returns tags of the document as one line of tags separated by commas
     *
     * @return line of tags
     */
    public String getTagsToString() {
        return tags;
    }

    /**
     * method that sets the document's tags
     *
     * @param tags - line of tags separated by commas
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * method that sets the document's tags
     *
     * @param tags - list of tags
     */
    public void setTags(LinkedList<String> tags) {
        for (String tag : tags) {
            this.tags = this.tags + tag;
        }
    }

    /**
     * method that adds an instance of document and user relations to the list of the document's relations
     *
     * @param documentUser - document and user relation that is to be added
     */
    public void addToQueue(DocumentUser documentUser) {
        users.add(documentUser);
    }

    /**
     * method that removes an nstance of document and user relations from the list of the document's relations
     *
     * @param documentUser - document and user relation that is to be removed
     */
    public void removeFromQueue(DocumentUser documentUser) {
        users.remove(documentUser);
    }

    /**
     * method that returns the document's authors as a line of their names separated by commas
     *
     * @return line of the document's authors names separated by commas
     */
    public String authorsToString() {
        String result = "";
        ListIterator<Author> iterator = authors.listIterator();
        Author author;
        while (iterator.hasNext()) {
            author = iterator.next();
            result += author.getName();
            if (iterator.hasNext()) {
                result += ", ";
            }
        }
        return result;
    }
}
