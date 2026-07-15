package com.coc.sheet.dto;

import java.time.Instant;

public record AuditEventResponse(Instant timestamp, String description) {
}
