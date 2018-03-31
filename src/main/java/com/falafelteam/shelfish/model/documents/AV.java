package com.falafelteam.shelfish.model.documents;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import lombok.Data;

import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

@Data
@Table(name = "Document")
public class AV extends Document {

    List<Author> authors;

    public AV(String name, boolean isBestseller, int copies, int price, boolean isReference, Author author) {
        super(name, isBestseller, copies, price, isReference);
        authors = new LinkedList<Author>();
        authors.add(author);
    }

    public AV(String name, boolean isBestseller, int copies, int price, boolean isReference, List<Author> author) {
        super(name, isBestseller, copies, price, isReference);
        authors = new LinkedList<Author>();
        authors.addAll(author);
    }
}
