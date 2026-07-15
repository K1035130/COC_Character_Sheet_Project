package com.coc.sheet.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.coc.sheet.model.User;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
