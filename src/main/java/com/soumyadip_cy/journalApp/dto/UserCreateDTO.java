package com.soumyadip_cy.journalApp.dto;

import org.bson.types.ObjectId;

import java.util.List;

public record UserCreateDTO(ObjectId id, String userName, String password, List<JournalEntryDTO> journalEntries, List<String> roles) {
}
