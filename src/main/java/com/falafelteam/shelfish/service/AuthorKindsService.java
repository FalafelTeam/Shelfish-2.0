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

    /**
     * method that saves author to the database
     * does not save if such author already exists
     *
     * @param author - author that is being saved
     */
    void saveAuthor(Author author) {
        if (authorRepository.findByName(author.getName()) == null) {
            authorRepository.save(author);
        } else if (authorRepository.findByName(author.getName()).getId() == (author.getId())) {
            authorRepository.save(author);
        }
    }

    /**
     * method that saves editor to the database
     * does not add if such editor already exists
     *
     * @param editor - editor that is being saved
     */
    void saveEditor(Editor editor) {
        if (editorRepository.findByName(editor.getName()) == null) {
            editorRepository.save(editor);
        } else if (editorRepository.findByName(editor.getName()).getId() == (editor.getId())) {
            editorRepository.save(editor);
        }
    }

    /**
     * method that saves publisher to the database
     * does not add if such publisher already exists
     *
     * @param publisher - publisher that is being saved
     */
    void savePublisher(Publisher publisher) {
        if (publisherRepository.findByName(publisher.getName()) == null) {
            publisherRepository.save(publisher);
        } else if (publisherRepository.findByName(publisher.getName()).getId() == (publisher.getId())) {
            publisherRepository.save(publisher);
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
