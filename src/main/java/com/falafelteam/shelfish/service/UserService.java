package com.falafelteam.shelfish.service;

import com.falafelteam.shelfish.model.users.User;
import com.falafelteam.shelfish.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getById(int id) throws Exception {
        User found = userRepository.findById(id);
        if (found != null) {
            return found;
        } else throw new Exception("User not found");
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    public User getByName(String name) throws Exception { User found = userRepository.findByName(name);
        if (found != null) {
            return found;
        } else throw new Exception("User not found"); }
}
