package com.falafelteam.shelfish.model.documents;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

@Data
@Table(name = "Document")
public class Book extends Document {

    @ManyToMany
    @JoinTable(name = "document_author", joinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id")) // почитать
            List<Author> authors;

    Publisher publisher;

    public Book(String name, boolean isBestseller, int copies, int price, boolean isReference, Author author, Publisher publisher) {
        super(name, isBestseller, copies, price, isReference);
        this.authors = new LinkedList<Author>();
        this.authors.add(author);
        this.publisher = publisher;
    }

    public Book(String name, boolean isBestseller, int copies, int price, boolean isReference, List<Author> authors, Publisher publisher) {
        super(name, isBestseller, copies, price, isReference);
        this.authors = new LinkedList<Author>();
        this.authors.addAll(authors);
        this.publisher = publisher;
    }
}
