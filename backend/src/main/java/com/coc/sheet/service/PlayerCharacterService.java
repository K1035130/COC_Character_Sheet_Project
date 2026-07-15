package com.coc.sheet.service;

import java.time.Instant;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.coc.sheet.dto.CharacterProfileRequest;
import com.coc.sheet.exception.InvalidAttributeException;
import com.coc.sheet.exception.ResourceNotFoundException;
import com.coc.sheet.model.Attribute;
import com.coc.sheet.model.AuditEvent;
import com.coc.sheet.model.PlayerCharacter;
import com.coc.sheet.model.Skill;
import com.coc.sheet.repository.PlayerCharacterRepository;

@Service
public class PlayerCharacterService {

    private final PlayerCharacterRepository repository;

    public PlayerCharacterService(PlayerCharacterRepository repository) {
        this.repository = repository;
    }

    public List<PlayerCharacter> listForOwner(String ownerId) {
        return repository.findByOwnerId(ownerId);
    }

    public PlayerCharacter getForOwner(String id, String ownerId) {
        return repository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Character not found: " + id));
    }

    public PlayerCharacter create(String ownerId, CharacterProfileRequest req) {
        PlayerCharacter character = new PlayerCharacter();
        character.setOwnerId(ownerId);
        character.setName(req.name());
        character.setOccupation(req.occupation());
        character.setAge(req.age());
        character.setGender(req.gender());

        Map<Attribute, Integer> attributes = new EnumMap<>(Attribute.class);
        for (Attribute attr : Attribute.values()) {
            attributes.put(attr, 0);
        }
        character.setAttributes(attributes);

        Instant now = Instant.now();
        character.setCreatedAt(now);
        character.setUpdatedAt(now);
        character.getAuditLog().add(new AuditEvent("Created character " + req.name()));

        return repository.save(character);
    }

    public PlayerCharacter updateProfile(String id, String ownerId, CharacterProfileRequest req) {
        PlayerCharacter character = getForOwner(id, ownerId);
        character.getAuditLog().add(new AuditEvent(
                "Updated profile: " + character.getName() + " -> " + req.name()));
        character.setName(req.name());
        character.setOccupation(req.occupation());
        character.setAge(req.age());
        character.setGender(req.gender());
        character.setBackstory(req.backstory());
        character.setUpdatedAt(Instant.now());
        return repository.save(character);
    }

    public PlayerCharacter updateAttribute(String id, String ownerId, Attribute attribute, int value) {
        if (attribute.isDerived()) {
            throw new InvalidAttributeException(
                    attribute + " is a derived attribute and cannot be set directly");
        }

        PlayerCharacter character = getForOwner(id, ownerId);
        int previous = character.getAttributes().getOrDefault(attribute, 0);
        character.getAttributes().put(attribute, value);
        recalculateDerivedAttributes(character);

        character.getAuditLog().add(new AuditEvent(
                "Attribute " + attribute + ": " + previous + " -> " + value));
        character.setUpdatedAt(Instant.now());
        return repository.save(character);
    }

    public PlayerCharacter updateAttributes(String id, String ownerId, Map<Attribute, Integer> values) {
        for (Attribute attribute : values.keySet()) {
            if (attribute.isDerived()) {
                throw new InvalidAttributeException(
                        attribute + " is a derived attribute and cannot be set directly");
            }
        }

        PlayerCharacter character = getForOwner(id, ownerId);
        character.getAttributes().putAll(values);
        recalculateDerivedAttributes(character);

        character.getAuditLog().add(new AuditEvent("Bulk attribute update (" + values.size() + " attributes)"));
        character.setUpdatedAt(Instant.now());
        return repository.save(character);
    }

    public PlayerCharacter updateAvatar(String id, String ownerId, String dataUrl) {
        PlayerCharacter character = getForOwner(id, ownerId);
        character.setAvatarDataUrl(dataUrl);
        character.getAuditLog().add(new AuditEvent("Updated avatar"));
        character.setUpdatedAt(Instant.now());
        return repository.save(character);
    }

    public PlayerCharacter upsertSkill(String id, String ownerId, String skillName, int addValue) {
        PlayerCharacter character = getForOwner(id, ownerId);
        Skill existing = findSkill(character, skillName);
        if (existing != null) {
            existing.setAddValue(addValue);
        } else {
            character.getSkills().add(new Skill(skillName, addValue));
        }
        character.getAuditLog().add(new AuditEvent("Skill updated: " + skillName));
        character.setUpdatedAt(Instant.now());
        return repository.save(character);
    }

    public PlayerCharacter deleteSkill(String id, String ownerId, String skillName) {
        PlayerCharacter character = getForOwner(id, ownerId);
        Skill existing = findSkill(character, skillName);
        if (existing != null) {
            character.getSkills().remove(existing);
            character.getAuditLog().add(new AuditEvent("Skill removed: " + skillName));
            character.setUpdatedAt(Instant.now());
            return repository.save(character);
        }
        return character;
    }

    public void delete(String id, String ownerId) {
        PlayerCharacter character = getForOwner(id, ownerId);
        repository.delete(character);
    }

    private Skill findSkill(PlayerCharacter character, String skillName) {
        for (Skill skill : character.getSkills()) {
            if (skill.getName().equalsIgnoreCase(skillName)) {
                return skill;
            }
        }
        return null;
    }

    private void recalculateDerivedAttributes(PlayerCharacter character) {
        Map<Attribute, Integer> attributes = character.getAttributes();
        int con = attributes.getOrDefault(Attribute.CON, 0);
        int siz = attributes.getOrDefault(Attribute.SIZ, 0);
        int pow = attributes.getOrDefault(Attribute.POW, 0);

        attributes.put(Attribute.HP, DerivedStatCalculator.calcHp(con, siz));
        attributes.put(Attribute.MP, DerivedStatCalculator.calcMp(pow));
        attributes.put(Attribute.SAN, DerivedStatCalculator.calcSan(pow));
    }
}
