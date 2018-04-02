package com.falafelteam.shelfish.repository;

import com.falafelteam.shelfish.model.DocumentUser;
import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.users.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentUserRepository extends CrudRepository<DocumentUser, Integer> {

    DocumentUser findByDocumentAndUser(Document document, User user);

    List<DocumentUser> findAllByDocument(Document document);

    List<DocumentUser> findAllByUser(User user);

    List<DocumentUser> findAllByDocumentAndStatus(Document document, String status);
}
