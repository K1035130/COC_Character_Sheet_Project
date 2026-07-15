package com.coc.sheet.model;

import java.time.Instant;

public class AuditEvent {

    private Instant timestamp;
    private String description;

    public AuditEvent() {
    }

    public AuditEvent(String description) {
        this.timestamp = Instant.now();
        this.description = description;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
