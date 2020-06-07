package com.outfit.fun7.service;

import com.outfit.fun7.repository.UserRepository;
import com.outfit.fun7.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        Optional<User> users = this.userRepository.findById(id);
        User user = null;
        if (users.isPresent()) {
            user = users.get();
        }
        return user;
    }
}
