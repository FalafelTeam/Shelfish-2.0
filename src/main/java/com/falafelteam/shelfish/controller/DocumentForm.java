package com.falafelteam.shelfish.controller;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import com.falafelteam.shelfish.model.documents.DocumentType;
import com.falafelteam.shelfish.repository.DocumentTypeRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    DocumentTypeRepository docType;

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
    private final List<String> TYPE;
    private String tags;
    private String authors;
    private String editor;
    private String publisher;

    public DocumentForm() {
        TYPE = new LinkedList<>();
        for (DocumentType document : docType.findAll()) {

        }
    }

    public LinkedList<Author> getParsedAuthors() {
        LinkedList<Author> authors = new LinkedList<>();
        String[] authorsString = this.authors.split(", ");
        for (String author : authorsString) {
            authors.add(new Author(author));
        }
        return authors;
    }

    public void validate() throws Exception {
        Pattern tagPattern = Pattern.compile("[a-z]+(, s*[a-z]+)*");
        Matcher tagMatcher = tagPattern.matcher(tags);
        if (!tagMatcher.matches()) {
            throw new Exception("Tags should be lower-case letters, in the format \"tag1, tag2, tag3, tag4\" ");
        }

        Pattern authorPattern = Pattern.compile("[A-Za-z ]+(, s*[A-Za-z ]+)*");
        Matcher authorMatcher = authorPattern.matcher(authors);
        if (!authorMatcher.matches()) {
            throw new Exception("Authors should be separated by a comma and a space");
        }

        Pattern editorPublisherPattern = Pattern.compile("[A-Za-z ]+");
        Matcher editorPublisherMatcher = editorPublisherPattern.matcher(editor);
        if (!editorPublisherMatcher.matches()) {
            throw new Exception("There can be only ONE editor");
        }

        editorPublisherMatcher = editorPublisherPattern.matcher(publisher);
        if (!editorPublisherMatcher.matches()) {
            throw new Exception("There can be only ONE publisher");
        }
    }
}
