package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.DocumentUser;
import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.users.User;
import com.falafelteam.shelfish.repository.DocumentUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentUserService {

    private final DocumentUserRepository documentUserRepository;

    @Autowired
    public DocumentUserService(DocumentUserRepository documentUserRepository) {
        this.documentUserRepository = documentUserRepository;
    }

    public List<DocumentUser> getByUser(User user) {
        return documentUserRepository.findAllByUser(user);
    }
}
