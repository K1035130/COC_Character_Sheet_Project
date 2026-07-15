package com.coc.sheet.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.coc.sheet.exception.InvalidAttributeException;
import com.coc.sheet.exception.ResourceNotFoundException;
import com.coc.sheet.model.Attribute;
import com.coc.sheet.model.PlayerCharacter;
import com.coc.sheet.security.JwtService;
import com.coc.sheet.service.PlayerCharacterService;

@WebMvcTest(PlayerCharacterController.class)
@AutoConfigureMockMvc(addFilters = false)
class PlayerCharacterControllerTest {

    private static final String OWNER_ID = "owner-1";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlayerCharacterService service;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void listReturnsEmptyArrayWhenNoCharacters() throws Exception {
        when(service.listForOwner(OWNER_ID)).thenReturn(List.of());

        mockMvc.perform(get("/api/characters").principal(() -> OWNER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getReturns404WhenCharacterNotFound() throws Exception {
        when(service.getForOwner("missing", OWNER_ID))
                .thenThrow(new ResourceNotFoundException("Character not found: missing"));

        mockMvc.perform(get("/api/characters/missing").principal(() -> OWNER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAttributeReturns400ForDerivedAttribute() throws Exception {
        when(service.updateAttribute(eq("char-1"), eq(OWNER_ID), eq(Attribute.HP), anyInt()))
                .thenThrow(new InvalidAttributeException("HP is a derived attribute and cannot be set directly"));

        mockMvc.perform(patch("/api/characters/char-1/attributes/HP")
                        .principal(() -> OWNER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"value\":10}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReturns201WithBody() throws Exception {
        PlayerCharacter created = new PlayerCharacter();
        created.setId("char-1");
        created.setOwnerId(OWNER_ID);
        created.setName("Owen");
        for (Attribute attribute : Attribute.values()) {
            created.getAttributes().put(attribute, 0);
        }

        when(service.create(eq(OWNER_ID), any())).thenReturn(created);

        mockMvc.perform(post("/api/characters")
                        .principal(() -> OWNER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Owen\",\"occupation\":\"Detective\",\"age\":30,\"gender\":\"male\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Owen"));
    }
}
