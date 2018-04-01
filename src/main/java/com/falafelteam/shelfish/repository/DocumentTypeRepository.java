package com.falafelteam.shelfish.repository;

import com.falafelteam.shelfish.model.documents.DocumentType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends CrudRepository<DocumentType, Integer> {

    DocumentType findByName(String name);
}
