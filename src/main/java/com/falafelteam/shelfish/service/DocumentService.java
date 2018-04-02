package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import com.falafelteam.shelfish.model.AuthorKinds.Editor;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service method for Document
 */
@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final AuthorKindsService authorKindsService;

    @Autowired
    public DocumentService(DocumentRepository documentRepository, AuthorKindsService authorKindsService) {
        this.documentRepository = documentRepository;
        this.authorKindsService = authorKindsService;
    }

    /**
     * method that gets document with stated id
     *
     * @param id - id of the document
     * @return document with stated id
     * @throws Exception if there is no document with such id
     */
    public Document getById(int id) throws Exception {
        if (documentRepository.findById(id) != null) {
            return documentRepository.findById(id);
        } else throw new Exception("Document not found");
    }

    public Document getByName(String name) throws Exception {
        if (documentRepository.findByName(name) != null) {
            return documentRepository.findByName(name);
        } else throw new Exception("Document not found");
    }

    /**
     * method that adds document to the database
     *
     * @param document - the document that is being added
     */
    public void add(Document document) {
        if (document.getAuthors() != null) {
            for (Author author : document.getAuthors()) {
                if (authorKindsService.getAuthorByName(author.getName()) == null) {
                    authorKindsService.saveAuthor(author);
                }
            }
        }
        if (document.getEditor() != null) {
            authorKindsService.saveEditor(document.getEditor());
        }
        if (document.getPublisher() != null) {
            authorKindsService.savePublisher(document.getPublisher());
        }
        documentRepository.save(document);
    }

    /**
     * method that modifies the document
     *
     * @param document    - the document that is being modified
     * @param newDocument - document that has attributes that are being assigned to the modified document
     */
    public void modify(Document document, Document newDocument) {
        document.setName(newDocument.getName());
        document.setDescription(newDocument.getDescription());
        document.setBestseller(newDocument.isBestseller());
        document.setCopies(newDocument.getCopies());
        document.setPrice(newDocument.getPrice());
        document.setReference(newDocument.isReference());
        document.setTags(newDocument.getTags());
        if (!document.getAuthors().isEmpty()) {
            document.setAuthors(newDocument.getAuthors());
        }
        if (document.getEditor() != null) {
            document.setEditor(newDocument.getEditor());
        }
        if (document.getPublisher() != null) {
            document.setPublisher(newDocument.getPublisher());
        }
        documentRepository.save(document);
    }

    /**
     * method that deletes the document
     *
     * @param id - id of the document that is being deleted
     */
    public void deleteById(int id) {
        Document found = documentRepository.findById(id);
        List<Author> authors = found.getAuthors();
        Editor editor = found.getEditor();
        Publisher publisher = found.getPublisher();
        authorKindsService.deleteRedundant(authors, editor, publisher);
        documentRepository.delete(found);
    }
}
