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

    public Document getById(int id) throws Exception {
        if (documentRepository.findById(id) != null) {
            return documentRepository.findById(id);
        } else throw new Exception("Document not found");
    }

    public void save(Document document) {
        documentRepository.save(document);
    }

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
    }

    public void deleteById(int id) {
        Document found = documentRepository.findById(id);
        List<Author> authors = found.getAuthors();
        Editor editor = found.getEditor();
        Publisher publisher = found.getPublisher();
        authorKindsService.deleteRedundant(authors, editor, publisher);
        documentRepository.delete(found);
    }
}
