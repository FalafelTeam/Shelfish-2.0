package com.falafelteam.shelfish.repository;

import com.falafelteam.shelfish.model.users.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    Role findByName(String name);
}
