package com.coc.sheet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CharacterProfileRequest(
        @NotBlank String name,
        String occupation,
        @PositiveOrZero int age,
        String gender,
        @Size(max = 5000) String backstory) {
}
