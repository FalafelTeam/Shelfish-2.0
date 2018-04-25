package com.falafelteam.shelfish.repository;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import com.falafelteam.shelfish.model.AuthorKinds.Editor;
import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import com.falafelteam.shelfish.model.documents.Document;
import com.falafelteam.shelfish.model.documents.DocumentType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.Date;
import java.util.List;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Integer> {

    Document findById(int id);

    List<Document> findAllByAuthorsContaining(Author author);

    List<Document> findAllByEditor(Editor editor);

    List<Document> findAllByPublisher(Publisher publisher);

    Document findByName(String name);

    List<Document> findAllByNameContainingAndType(String name, DocumentType documentType);

    List<Document> findAllByPublishingDateAndType(Date date, DocumentType documentType);

    List<Document> findAllByPublisherAndType(Publisher publisher, DocumentType documentType);

    List<Document> findAllByAuthorsContainingAndType(String authors, DocumentType documentType);

    List<Document> findAllByNameContaining(String name);

    List<Document> findAllByPublishingDate(Date publicationDate);

    List<Document> findAllByAuthorsContaining(String authors);

    List<Document> findAllByNameContainingOrAuthorsContainingOrPublisherContaining(String name, @Nullable Author author, @Nullable Publisher publisher);
}
