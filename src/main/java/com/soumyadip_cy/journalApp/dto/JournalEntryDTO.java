package com.soumyadip_cy.journalApp.dto;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public record JournalEntryDTO (ObjectId id, String title, String content, LocalDateTime date) {}
