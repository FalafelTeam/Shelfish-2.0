package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document getById(int id) throws Exception {
        Document found = documentRepository.findById(id);
        if (found != null) {
            return found;
        } else throw new Exception("Document not found");
    }

    public void save(Document document) {
        documentRepository.save(document);
    }

    public void deleteById(int id) {
        documentRepository.deleteById(id);
    }
}
