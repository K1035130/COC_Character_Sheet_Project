package com.coc.sheet.service;

import java.time.Instant;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coc.sheet.exception.DuplicateUsernameException;
import com.coc.sheet.model.User;
import com.coc.sheet.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String rawPassword, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException("Username already taken: " + username);
        }
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setEmail(email);
        user.setCreatedAt(Instant.now());
        return userRepository.save(user);
    }
}
