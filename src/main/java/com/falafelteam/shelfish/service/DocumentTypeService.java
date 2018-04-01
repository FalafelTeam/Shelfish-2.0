package com.falafelteam.shelfish.service;

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

    public List<String> getAllTypes() {
        List<String> result = new LinkedList<>();
        documentTypeRepository.findAll().forEach(documentType -> result.add(documentType.getName()));
        return result;
    }
}
