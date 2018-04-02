package com.falafelteam.shelfish.model.users;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Class for the role entity
 *
 * Priority of roles:
 * 0 - "Librarian"
 * 1 - "Student"
 * 2 - "Instructor"
 * 3 - "TA"
 * 4 - "Visiting Professor"
 * 5 - "Professor"
 */
@Data
@Entity
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private int priority;

    public Role(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }
}
