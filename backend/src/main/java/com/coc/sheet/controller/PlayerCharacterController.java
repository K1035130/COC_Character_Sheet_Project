package com.coc.sheet.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coc.sheet.dto.AttributeUpdateRequest;
import com.coc.sheet.dto.AvatarUpdateRequest;
import com.coc.sheet.dto.CharacterProfileRequest;
import com.coc.sheet.dto.CharacterResponse;
import com.coc.sheet.dto.SkillRequest;
import com.coc.sheet.model.Attribute;
import com.coc.sheet.model.PlayerCharacter;
import com.coc.sheet.service.PlayerCharacterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/characters")
public class PlayerCharacterController {

    private final PlayerCharacterService service;

    public PlayerCharacterController(PlayerCharacterService service) {
        this.service = service;
    }

    @GetMapping
    public List<CharacterResponse> list(Principal principal) {
        return service.listForOwner(principal.getName()).stream()
                .map(CharacterResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<CharacterResponse> create(
            Principal principal, @Valid @RequestBody CharacterProfileRequest request) {
        PlayerCharacter created = service.create(principal.getName(), request);
        return ResponseEntity.status(201).body(CharacterResponse.from(created));
    }

    @GetMapping("/{id}")
    public CharacterResponse get(Principal principal, @PathVariable String id) {
        return CharacterResponse.from(service.getForOwner(id, principal.getName()));
    }

    @PutMapping("/{id}")
    public CharacterResponse updateProfile(
            Principal principal, @PathVariable String id, @Valid @RequestBody CharacterProfileRequest request) {
        return CharacterResponse.from(service.updateProfile(id, principal.getName(), request));
    }

    @PatchMapping("/{id}/attributes/{attr}")
    public CharacterResponse updateAttribute(
            Principal principal, @PathVariable String id, @PathVariable String attr,
            @Valid @RequestBody AttributeUpdateRequest request) {
        Attribute attribute = Attribute.valueOf(attr.toUpperCase());
        return CharacterResponse.from(
                service.updateAttribute(id, principal.getName(), attribute, request.value()));
    }

    @PatchMapping("/{id}/attributes")
    public CharacterResponse updateAttributes(
            Principal principal, @PathVariable String id, @RequestBody Map<Attribute, Integer> values) {
        return CharacterResponse.from(service.updateAttributes(id, principal.getName(), values));
    }

    @PutMapping("/{id}/avatar")
    public CharacterResponse updateAvatar(
            Principal principal, @PathVariable String id, @Valid @RequestBody AvatarUpdateRequest request) {
        return CharacterResponse.from(service.updateAvatar(id, principal.getName(), request.dataUrl()));
    }

    @PutMapping("/{id}/skills/{skillName}")
    public CharacterResponse upsertSkill(
            Principal principal, @PathVariable String id, @PathVariable String skillName,
            @Valid @RequestBody SkillRequest request) {
        return CharacterResponse.from(
                service.upsertSkill(id, principal.getName(), skillName, request.addValue()));
    }

    @DeleteMapping("/{id}/skills/{skillName}")
    public CharacterResponse deleteSkill(
            Principal principal, @PathVariable String id, @PathVariable String skillName) {
        return CharacterResponse.from(service.deleteSkill(id, principal.getName(), skillName));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Principal principal, @PathVariable String id) {
        service.delete(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
