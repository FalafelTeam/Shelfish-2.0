package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.users.Role;
import com.falafelteam.shelfish.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Service class for Role
 */
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * method that adds a new role (does nothing if there is a role with the same name)
     *
     * @param name     - name of the role to be added
     * @param priority - priority of the role to be added
     */
    public void add(String name, int priority) {
        if (!getAllRoles().contains(name)) {
            roleRepository.save(new Role(name, priority));
        }
    }

    /**
     * method that gets names of all roles
     *
     * @return - list of roles' names
     */
    public List<String> getAllRoles() {
        List<String> result = new LinkedList<>();
        roleRepository.findAll().forEach(role -> result.add(role.getName()));
        return result;
    }

    /**
     * method that gets a role by its name
     *
     * @param name - name of the role to be found
     * @return role with the stated name
     */
    public Role getByName(String name) {
        return roleRepository.findByName(name);
    }
}
