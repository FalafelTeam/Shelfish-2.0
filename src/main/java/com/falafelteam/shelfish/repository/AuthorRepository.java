package com.falafelteam.shelfish.repository;

import com.falafelteam.shelfish.model.AuthorKinds.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Integer> {

    Author findByName(String name);
}
