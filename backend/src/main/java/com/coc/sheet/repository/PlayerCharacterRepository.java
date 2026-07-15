package com.coc.sheet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.coc.sheet.model.PlayerCharacter;

public interface PlayerCharacterRepository extends MongoRepository<PlayerCharacter, String> {

    List<PlayerCharacter> findByOwnerId(String ownerId);

    Optional<PlayerCharacter> findByIdAndOwnerId(String id, String ownerId);
}
