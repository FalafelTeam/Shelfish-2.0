package com.falafelteam.shelfish.repository;

import com.falafelteam.shelfish.model.AuthorKinds.Publisher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends CrudRepository<Publisher, Integer> {

    Publisher findByName(String name);

    Publisher findByNameContaining(String name);
}
