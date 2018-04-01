package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.DocumentUser;
import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.users.User;
import com.falafelteam.shelfish.repository.DocumentUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final DocumentService documentService;
    private final UserService userService;
    private final DocumentUserRepository documentUserRepository;

    @Autowired
    public BookingService(DocumentService documentService, UserService userService, DocumentUserRepository documentUserRepository) {
        this.documentService = documentService;
        this.userService = userService;
        this.documentUserRepository = documentUserRepository;
    }

    public void book(Document document, User user) throws Exception {
        if (user.getRole().getPriority() == 0) {
            outstandingRequest(document);
            return;
        }
        if (document.isReference()) {
            throw new Exception("The document is reference material");
        }
        if (documentUserRepository.findByDocumentAndUser(document, user) != null) {
            throw new Exception("Cannot book several copies of the document");
        }
    }

    public void checkOut(Document document, User user) {
    }

    private void outstandingRequest(Document document) {
    }
}
