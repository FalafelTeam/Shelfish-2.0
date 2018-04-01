package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.documents.DocumentType;
import com.falafelteam.shelfish.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;

    @Autowired
    public DocumentTypeService(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    public void add(String name) {
        if (!getAllTypes().contains(name)) {
            documentTypeRepository.save(new DocumentType(name));
        }
    }

    public List<String> getAllTypes() {
        List<String> result = new LinkedList<>();
        documentTypeRepository.findAll().forEach(documentType -> result.add(documentType.getName()));
        return result;
    }

    public DocumentType getByName(String name) {
        return documentTypeRepository.findByName(name);
    }
}
