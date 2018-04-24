package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.users.User;
import com.falafelteam.shelfish.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for User
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User getById(int id) throws Exception {
        User found = userRepository.findById(id);
        if (found != null) {
            return found;
        } else throw new Exception("User not found");
    }

    public void register(User user) throws Exception {
        User found = userRepository.findByLogin(user.getLogin());
        if (found != null) {
            throw new Exception("User with such login already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * method that saves user to the database
     *
     * @param user - the user that is to be saved
     */
    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * method that deletes user by its id
     *
     * @param id - id of the user that is to be deleted
     */
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    /**
     * method that gets the user by its name
     *
     * @param name - name of the user to be found
     * @return user with the stated name
     * @throws Exception "User not found"
     */
    public User getByName(String name) throws Exception {
        User found = userRepository.findByName(name);
        if (found != null) {
            return found;
        } else throw new Exception("User not found");
    }
}
