package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.documents.DocumentType;
import com.falafelteam.shelfish.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Service class for the DocumentType
 */
@Service
public class DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;

    @Autowired
    public DocumentTypeService(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    /**
     * method that adds new document type (does nothing if there is a type with the same name)
     *
     * @param name - name of the type to be added
     */
    public void add(String name) {
        if (!getAllTypes().contains(name)) {
            documentTypeRepository.save(new DocumentType(name));
        }
    }

    /**
     * method that gets names of all types
     *
     * @return - list of document types' names
     */
    public List<String> getAllTypes() {
        List<String> result = new LinkedList<>();
        documentTypeRepository.findAll().forEach(documentType -> result.add(documentType.getName()));
        return result;
    }

    /**
     * method that gets a document type by its name
     *
     * @param name - name of the type to be found
     * @return document type with stated name
     */
    public DocumentType getByName(String name) {
        return documentTypeRepository.findByName(name);
    }
}
