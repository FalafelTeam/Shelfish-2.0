package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import com.falafelteam.shelfish.model.AuthorKinds.Editor;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.documents.DocumentType;
import com.falafelteam.shelfish.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Service method for Document
 */
@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final AuthorKindsService authorKindsService;
    private final DocumentTypeService documentTypeService;

    @Autowired
    public DocumentService(DocumentRepository documentRepository, AuthorKindsService authorKindsService, DocumentTypeService documentTypeService) {
        this.documentRepository = documentRepository;
        this.authorKindsService = authorKindsService;
        this.documentTypeService = documentTypeService;
    }

    /**
     * method that gets document with stated id
     *
     * @param id - id of the document
     * @return document with stated id
     * @throws Exception "Document not found"
     */
    public Document getById(int id) throws Exception {
        if (documentRepository.findById(id) != null) {
            return documentRepository.findById(id);
        } else throw new Exception("Document not found");
    }

    /**
     * method that gets document with stated name
     *
     * @param name - name of the document
     * @return document with stated name
     * @throws Exception "Document not found"
     */
    public Document getByName(String name) throws Exception {
        if (documentRepository.findByName(name) != null) {
            return documentRepository.findByName(name);
        } else throw new Exception("Document not found");
    }

    public Iterable<Document> getAll() {
        return documentRepository.findAll();
    }

    /**
     * method that adds document to the database
     *
     * @param document - the document that to be added
     */
    public void add(Document document) {
        if (document.getAuthors() != null) {
            LinkedList<Author> authors = new LinkedList<>();
            for (Author author : document.getAuthors()) {
                if (authorKindsService.getAuthorByName(author.getName()) == null) {
                    author = authorKindsService.saveAuthor(author);
                }
                authors.add(author);
            }
            document.setAuthors(authors);
        }
        if (document.getEditor() != null) {
            Editor editor = authorKindsService.saveEditor(document.getEditor());
            document.setEditor(editor);
        }
        if (document.getPublisher() != null) {
            Publisher publisher = authorKindsService.savePublisher(document.getPublisher());
            document.setPublisher(publisher);
        }
        documentRepository.save(document);
    }

    /**
     * method that modifies the document
     *
     * @param document    - the document that is to be modified
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
     * method that deletes the document by its id
     *
     * @param id - id of the document that is to be deleted
     */
    public void deleteById(int id) {
        Document found = documentRepository.findById(id);
        List<Author> authors = found.getAuthors();
        Editor editor = found.getEditor();
        Publisher publisher = found.getPublisher();
        authorKindsService.deleteRedundant(authors, editor, publisher);
        documentRepository.delete(found);
    }

    public List<Document> searchByName(String name, String type){
        DocumentType docType = documentTypeService.getByName(type);
        return documentRepository.findAllByNameContainingAndType(name, docType);
    }

    public List<Document> searchByName(String name){
        return documentRepository.findAllByNameContaining(name);
    }

    public List<Document> searchByAuthor(String authors, String type){
        DocumentType docType = documentTypeService.getByName(type);
        return documentRepository.findAllByAuthorsContainingAndType(authors, docType);
    }

    public List<Document> searchByAuthor(String authors){
        return documentRepository.findAllByAuthorsContaining(authors);
    }

    public List<Document> searchByPublishingDate(Date date, String type){
        DocumentType docType = documentTypeService.getByName(type);
        return documentRepository.findAllByPublishingDateAndType(date, docType);
    }

    public List<Document> searchByPublishingDate(Date date){
        return documentRepository.findAllByPublishingDate(date);
    }

    public List<Document> searchByPublisher(Publisher publisher, String type){
        DocumentType docType = documentTypeService.getByName(type);
        return documentRepository.findAllByPublisherAndType(publisher, docType);
    }
    public List<Document> searchByPublisher(Publisher publisher){
        return documentRepository.findAllByPublisher(publisher);
    }

    public List<Document> searchByAll(String search) {
        Author author = authorKindsService.getAuthorByNameContatinig(search);
        Publisher publisher = authorKindsService.getPublisherByNameContaining(search);
        return documentRepository.findAllByNameContainingOrAuthorsContainingOrPublisherContaining(search, author, publisher);
    }
}
