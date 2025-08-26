package com.soumyadip_cy.journalApp.dto;

import org.bson.types.ObjectId;

import java.util.List;

public record UserDTO(ObjectId id, String userName, List<JournalEntryDTO> journalEntries, List<String> roles) {
}
