package com.falafelteam.shelfish.model.documents;

import com.falafelteam.shelfish.model.AuthorKinds.Editor;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "Document")
public class Article extends Document {

    Editor editor;
    Publisher publisher;

    public Article(String name, boolean isBestseller, int copies, int price, boolean isReference, Editor editor, Publisher publisher) {
        super(name, isBestseller, copies, price, isReference);
        this.editor = editor;
        this.publisher = publisher;
    }
}
