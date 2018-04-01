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
 * 0 - "librarian"
 * 1 - "student"
 * 2 - "instructor"
 * 3 - "ta"
 * 4 - "visiting professor"
 * 5 - "professor"
 */
@Data
@Entity
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private int priority;

    public Role(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }
}
