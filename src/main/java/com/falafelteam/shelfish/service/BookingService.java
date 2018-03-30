package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserService userService;

    public void book(Document document, User user) {
    }

    public void checkOut(Document document, User user) {
    }

    public void outstandingRequest(int id) {
    }
}
