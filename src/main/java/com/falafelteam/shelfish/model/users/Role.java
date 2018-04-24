package com.falafelteam.shelfish.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Class for the role model
 *
 * Priority of roles:
 * 0 - "Librarian"/"Admin"
 * 1 - "Student"
 * 2 - "Instructor"
 * 3 - "TA"
 * 4 - "Visiting Professor"
 * 5 - "Professor"
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private int priority;

    public Role(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
