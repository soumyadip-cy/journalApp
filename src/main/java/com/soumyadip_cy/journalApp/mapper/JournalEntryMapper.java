package com.soumyadip_cy.journalApp.mapper;

import com.soumyadip_cy.journalApp.dto.JournalEntryDTO;
import com.soumyadip_cy.journalApp.entity.JournalEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JournalEntryMapper {
    JournalEntryMapper INSTANCE = Mappers.getMapper(JournalEntryMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "date", source = "date")
    JournalEntryDTO toJournalEntryDTO(JournalEntry journalEntry);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "date", source = "date")
    JournalEntry toJournalEntry(JournalEntryDTO journalEntryDTO);
}
