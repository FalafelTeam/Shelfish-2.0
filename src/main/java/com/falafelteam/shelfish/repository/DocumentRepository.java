package com.falafelteam.shelfish.repository;

import com.falafelteam.shelfish.model.documents.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Integer>{

    Document findById(int id);
}
