package com.falafelteam.shelfish.repository;

import com.falafelteam.shelfish.model.AuthorKinds.Editor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorRepository extends CrudRepository<Editor, Integer> {

    Editor findByName(String name);
}
