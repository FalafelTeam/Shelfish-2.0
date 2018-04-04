package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import com.falafelteam.shelfish.model.AuthorKinds.Editor;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.repository.AuthorRepository;
import com.falafelteam.shelfish.repository.DocumentRepository;
import com.falafelteam.shelfish.repository.EditorRepository;
import com.falafelteam.shelfish.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for AuthorKinds
 */
@Service
public class AuthorKindsService {

    private final AuthorRepository authorRepository;
    private final EditorRepository editorRepository;
    private final PublisherRepository publisherRepository;
    private final DocumentRepository documentRepository;

    @Autowired
    public AuthorKindsService(AuthorRepository authorRepository, EditorRepository editorRepository,
                              PublisherRepository publisherRepository, DocumentRepository documentRepository) {
        this.authorRepository = authorRepository;
        this.editorRepository = editorRepository;
        this.publisherRepository = publisherRepository;
        this.documentRepository = documentRepository;
    }

    Author getAuthorByName(String name) {
        return authorRepository.findByName(name);
    }

    Editor getEditorByName(String name) {
        return editorRepository.findByName(name);
    }

    Publisher getPublisherByName(String name) {
        return publisherRepository.findByName(name);
    }

    /**
     * method that saves author to the database
     * does not save if such author already exists
     *
     * @param author - author that is being saved
     */
    Author saveAuthor(Author author) {
        if (authorRepository.findByName(author.getName()) == null) {
            authorRepository.save(author);
            return author;
        } else {
            return authorRepository.findByName(author.getName());
        }
    }

    /**
     * method that saves editor to the database
     * does not add if such editor already exists
     *
     * @param editor - editor that is being saved
     */
    Editor saveEditor(Editor editor) {
        if (editorRepository.findByName(editor.getName()) == null) {
            editorRepository.save(editor);
            return editor;
        } else {
            return editorRepository.findByName(editor.getName());
        }
    }

    /**
     * method that saves publisher to the database
     * does not add if such publisher already exists
     *
     * @param publisher - publisher that is being saved
     */
    Publisher savePublisher(Publisher publisher) {
        if (publisherRepository.findByName(publisher.getName()) == null) {
            publisherRepository.save(publisher);
            return publisher;
        } else {
            return publisherRepository.findByName(publisher.getName());
        }
    }

    /**
     * method that checks and deletes redundant entities
     *
     * @param authors   - authors that are being checked
     * @param editor    - editor that is being checked
     * @param publisher - publisher that is being checked
     */
    void deleteRedundant(List<Author> authors, Editor editor, Publisher publisher) {
        for (Author author : authors) {
            if (documentRepository.findAllByAuthorsContaining(author) == null) {
                authorRepository.delete(author);
            }
        }
        if (documentRepository.findAllByEditor(editor) == null) {
            editorRepository.delete(editor);
        }
        if (documentRepository.findAllByPublisher(publisher) == null) {
            publisherRepository.delete(publisher);
        }
    }
}
