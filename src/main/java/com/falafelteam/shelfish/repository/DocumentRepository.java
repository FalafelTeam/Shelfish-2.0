package com.falafelteam.shelfish.repository;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import com.falafelteam.shelfish.model.AuthorKinds.Editor;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.documents.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Integer>{

    Document findById(int id);

    List<Document> findAllByAuthorsContaining(Author author);

    List<Document> findAllByEditor(Editor editor);

    List<Document> findAllByPublisher(Publisher publisher);
}
