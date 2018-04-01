package com.falafelteam.shelfish.controller;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * type1 - Article
 * type2 - AV
 * type3 - Book
 */
@Data
class DocumentForm {

    @NotEmpty
    private String name;
    private String description;
    private boolean isBestseller;
    @Positive
    private int copies;
    @Positive
    private int price;
    private boolean isReference;
    @Positive
    private int type;
    private String tags;
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

    public void validate(){
        Pattern tagPattern = Pattern.compile("[\\w]");
        //Matcher matcher = tagPattern.matcher();
    }
}
