package com.falafelteam.shelfish.repository;

import com.falafelteam.shelfish.model.DocumentUser;
import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.users.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentUserRepository extends CrudRepository<DocumentUser, Integer> {

    DocumentUser findByDocumentAndUser(Document document, User user);
}
