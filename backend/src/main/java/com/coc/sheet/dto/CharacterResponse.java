package com.coc.sheet.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.coc.sheet.model.Attribute;
import com.coc.sheet.model.PlayerCharacter;

public record CharacterResponse(
        String id,
        String name,
        String occupation,
        int age,
        String gender,
        String backstory,
        String avatarDataUrl,
        Map<Attribute, Integer> attributes,
        List<SkillResponse> skills,
        List<AuditEventResponse> auditLog,
        Instant createdAt,
        Instant updatedAt) {

    public static CharacterResponse from(PlayerCharacter character) {
        List<SkillResponse> skills = character.getSkills().stream()
                .map(s -> new SkillResponse(s.getName(), s.getAddValue(), s.getTotalValue()))
                .toList();
        List<AuditEventResponse> auditLog = character.getAuditLog().stream()
                .map(e -> new AuditEventResponse(e.getTimestamp(), e.getDescription()))
                .toList();
        return new CharacterResponse(
                character.getId(),
                character.getName(),
                character.getOccupation(),
                character.getAge(),
                character.getGender(),
                character.getBackstory(),
                character.getAvatarDataUrl(),
                character.getAttributes(),
                skills,
                auditLog,
                character.getCreatedAt(),
                character.getUpdatedAt());
    }
}
