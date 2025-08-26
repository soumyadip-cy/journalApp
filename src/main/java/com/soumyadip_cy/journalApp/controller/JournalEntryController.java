package com.soumyadip_cy.journalApp.controller;

import com.soumyadip_cy.journalApp.dto.JournalEntryDTO;
import com.soumyadip_cy.journalApp.dto.UserDTO;
import com.soumyadip_cy.journalApp.entity.JournalEntry;
import com.soumyadip_cy.journalApp.entity.User;
import com.soumyadip_cy.journalApp.mapper.JournalEntryMapper;
import com.soumyadip_cy.journalApp.mapper.UserMapper;
import com.soumyadip_cy.journalApp.service.JournalEntryService;
import com.soumyadip_cy.journalApp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
@Slf4j
@RequiredArgsConstructor
public class JournalEntryController {

    private final JournalEntryService journalEntryService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Optional<User> user = userService.findByUserName(authentication.getName());
            if(user.isPresent()) {
                UserDTO userDTO = UserMapper.INSTANCE.toUserDTO(user.get());
                List<JournalEntryDTO> allEntries = userDTO.journalEntries();
                if (allEntries != null && !allEntries.isEmpty()) {
                    log.info("Showing all journal entries !");
                    return new ResponseEntity<>(allEntries, HttpStatus.OK);
                }
            }
            log.info("No journal entries found !");
            return new ResponseEntity<>("No entries found", HttpStatus.NOT_FOUND);
            // Another approach
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No journal entries found.");
        } catch (Exception e) {
            log.error("Exception occurred while fetching journal entries",e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<JournalEntryDTO> createEntry(@RequestBody JournalEntryDTO myEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            journalEntryService.saveEntry(JournalEntryMapper.INSTANCE.toJournalEntry(myEntry), authentication.getName());
            log.info("Journal entry created !");
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Exception occurred while creating journal entry", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId myId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUserName(username).get();
            List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).toList();
            if(!collect.isEmpty()) {
                Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
                if(journalEntry.isPresent()) {
                    log.info("Journal entry found !");
                    return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            log.error("Exception occurred while fetching journal entry !",e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("Journal entry not found !");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    // Returns request as ResponseEntity<Object>
//    @GetMapping("/id/{myId}")
//    public ResponseEntity<Object> getByIdR(@PathVariable long myId) {
//        Object obj = journalEntries.get(myId);
//        return obj != null ? ResponseEntity.ok(obj) : ResponseEntity.notFound().build();
//    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<String> deleteById(@PathVariable ObjectId myId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            boolean removed = journalEntryService.deleteById(myId, userName);
            log.info("Journal entry deletion completed with result: '{}'", removed);
            if(removed)
                return new ResponseEntity<>("Requested Journal Entry is deleted !", HttpStatus.OK);
            else
                return new ResponseEntity<>("Requested Journal Entry is not found !", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred while deleting journal entry !",e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> updateById(
            @PathVariable ObjectId myId,
            @RequestBody JournalEntryDTO changedEntry
    ) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findByUserName(authentication.getName()).get();
            List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).toList();
            if(!collect.isEmpty()) {
                JournalEntry entry = journalEntryService.findById(myId).orElse(null);
                if (entry != null) {
                    entry.setTitle(changedEntry.title() != null && !changedEntry.title().isEmpty() ? changedEntry.title() : entry.getTitle());
                    entry.setContent(changedEntry.content() != null && !changedEntry.content().isEmpty() ? changedEntry.content() : entry.getContent());
                    journalEntryService.saveEntry(entry);
                    log.info("Journal entry edited !");
                    return new ResponseEntity<>(entry, HttpStatus.CREATED);
                }
            }
            log.info("Journal entry not found for editing !");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred while editing journal entry !",e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
