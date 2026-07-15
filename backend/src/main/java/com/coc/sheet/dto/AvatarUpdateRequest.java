package com.coc.sheet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AvatarUpdateRequest(
        @NotBlank @Size(max = 2_000_000) String dataUrl) {
}
