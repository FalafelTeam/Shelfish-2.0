package com.falafelteam.shelfish.controller;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import lombok.Data;

import java.util.LinkedList;

@Data
class DocumentForm {

    private String name;
    private String description;
    private Boolean isBestseller;
    private Integer copies;
    private Integer price;
    private Boolean isReference;
    private int type;
    private String authors;
    private String editor;
    private String publisher;

    public LinkedList<Author> getParsedAuthors() {
        LinkedList<Author> authors = new LinkedList<>();
        String[] authorsString = this.authors.split(", ");
        for (String author: authorsString) {
            authors.add(new Author(author));
        }
        return authors;
    }
}
