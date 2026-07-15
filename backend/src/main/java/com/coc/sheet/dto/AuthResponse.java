package com.coc.sheet.dto;

import java.time.Instant;

public record AuthResponse(String token, String username, Instant expiresAt) {
}
