package com.coc.sheet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coc.sheet.dto.CharacterProfileRequest;
import com.coc.sheet.exception.InvalidAttributeException;
import com.coc.sheet.exception.ResourceNotFoundException;
import com.coc.sheet.model.Attribute;
import com.coc.sheet.model.PlayerCharacter;
import com.coc.sheet.repository.PlayerCharacterRepository;

@ExtendWith(MockitoExtension.class)
class PlayerCharacterServiceTest {

    private static final String OWNER_ID = "owner-1";

    @Mock
    private PlayerCharacterRepository repository;

    @InjectMocks
    private PlayerCharacterService service;

    @BeforeEach
    void stubSave() {
        lenient().when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void createInitializesAllAttributesToZero() {
        PlayerCharacter created = service.create(
                OWNER_ID, new CharacterProfileRequest("Owen", "Detective", 30, "male", null));

        assertEquals(OWNER_ID, created.getOwnerId());
        for (Attribute attribute : Attribute.values()) {
            assertEquals(0, created.getAttributes().get(attribute));
        }
        assertEquals(1, created.getAuditLog().size());
    }

    @Test
    void getForOwnerThrowsWhenMissingOrWrongOwner() {
        when(repository.findByIdAndOwnerId("missing-id", OWNER_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getForOwner("missing-id", OWNER_ID));
    }

    @Test
    void updateAttributeRejectsDerivedAttributes() {
        assertThrows(InvalidAttributeException.class,
                () -> service.updateAttribute("id-1", OWNER_ID, Attribute.HP, 10));
    }

    @Test
    void updateAttributeRecalculatesDerivedStats() {
        PlayerCharacter character = service.create(
                OWNER_ID, new CharacterProfileRequest("Lily", "Nurse", 25, "female", null));
        when(repository.findByIdAndOwnerId(character.getId(), OWNER_ID))
                .thenReturn(Optional.of(character));

        service.updateAttribute(character.getId(), OWNER_ID, Attribute.CON, 60);
        service.updateAttribute(character.getId(), OWNER_ID, Attribute.SIZ, 40);
        PlayerCharacter updated = service.updateAttribute(character.getId(), OWNER_ID, Attribute.POW, 50);

        assertEquals(10, updated.getAttributes().get(Attribute.HP));
        assertEquals(10, updated.getAttributes().get(Attribute.MP));
        assertEquals(99, updated.getAttributes().get(Attribute.SAN));
    }

    @Test
    void updateAttributesBulkRejectsDerivedAttributes() {
        Map<Attribute, Integer> values = new EnumMap<>(Attribute.class);
        values.put(Attribute.STR, 50);
        values.put(Attribute.HP, 10);

        assertThrows(InvalidAttributeException.class,
                () -> service.updateAttributes("id-1", OWNER_ID, values));
    }

    @Test
    void updateAttributesBulkSetsAllAndRecalculatesDerivedStats() {
        PlayerCharacter character = service.create(
                OWNER_ID, new CharacterProfileRequest("Nora", "Librarian", 22, "female", null));
        when(repository.findByIdAndOwnerId(character.getId(), OWNER_ID))
                .thenReturn(Optional.of(character));

        Map<Attribute, Integer> values = new EnumMap<>(Attribute.class);
        values.put(Attribute.CON, 60);
        values.put(Attribute.SIZ, 40);
        values.put(Attribute.POW, 50);

        PlayerCharacter updated = service.updateAttributes(character.getId(), OWNER_ID, values);

        assertEquals(60, updated.getAttributes().get(Attribute.CON));
        assertEquals(40, updated.getAttributes().get(Attribute.SIZ));
        assertEquals(50, updated.getAttributes().get(Attribute.POW));
        assertEquals(10, updated.getAttributes().get(Attribute.HP));
        assertEquals(10, updated.getAttributes().get(Attribute.MP));
        assertEquals(99, updated.getAttributes().get(Attribute.SAN));
    }

    @Test
    void updateAvatarStoresDataUrl() {
        PlayerCharacter character = service.create(
                OWNER_ID, new CharacterProfileRequest("Sam", "Artist", 33, "male", null));
        when(repository.findByIdAndOwnerId(character.getId(), OWNER_ID))
                .thenReturn(Optional.of(character));

        PlayerCharacter updated = service.updateAvatar(character.getId(), OWNER_ID, "data:image/png;base64,abc123");

        assertEquals("data:image/png;base64,abc123", updated.getAvatarDataUrl());
    }

    @Test
    void upsertSkillAddsThenUpdatesInPlace() {
        PlayerCharacter character = service.create(
                OWNER_ID, new CharacterProfileRequest("Alex", "Student", 20, "other", null));
        when(repository.findByIdAndOwnerId(character.getId(), OWNER_ID))
                .thenReturn(Optional.of(character));

        PlayerCharacter afterAdd = service.upsertSkill(character.getId(), OWNER_ID, "Listen", 20);
        assertEquals(1, afterAdd.getSkills().size());
        assertEquals(25, afterAdd.getSkills().get(0).getTotalValue());

        PlayerCharacter afterUpdate = service.upsertSkill(character.getId(), OWNER_ID, "listen", 40);
        assertEquals(1, afterUpdate.getSkills().size());
        assertEquals(45, afterUpdate.getSkills().get(0).getTotalValue());
    }

    @Test
    void deleteSkillRemovesMatchingSkillCaseInsensitively() {
        PlayerCharacter character = service.create(
                OWNER_ID, new CharacterProfileRequest("Kevin", "Engineer", 28, "male", null));
        when(repository.findByIdAndOwnerId(character.getId(), OWNER_ID))
                .thenReturn(Optional.of(character));

        service.upsertSkill(character.getId(), OWNER_ID, "History", 10);
        assertTrue(character.getSkills().stream().anyMatch(s -> s.getName().equals("History")));

        service.deleteSkill(character.getId(), OWNER_ID, "HISTORY");
        assertFalse(character.getSkills().stream().anyMatch(s -> s.getName().equalsIgnoreCase("History")));
    }
}
