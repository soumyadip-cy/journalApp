package com.soumyadip_cy.journalApp.controller;

import com.soumyadip_cy.journalApp.entity.JournalEntry;
import com.soumyadip_cy.journalApp.entity.User;
import com.soumyadip_cy.journalApp.service.JournalEntryService;
import com.soumyadip_cy.journalApp.service.UserService;
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
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Optional<User> user = userService.findByUserName(authentication.getName());
            if(user.isPresent()) {
                List<JournalEntry> allEntries = user.get().getJournalEntries();
                if (allEntries != null && !allEntries.isEmpty()) {
                    return new ResponseEntity<>(allEntries, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>("No entries found", HttpStatus.NOT_FOUND);
            // Another approach
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No journal entries found.");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            journalEntryService.saveEntry(myEntry, authentication.getName());
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId myId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUserName(username).get();
            List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
            if(!collect.isEmpty()) {
                Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
                if(journalEntry.isPresent())
                    return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    // Returns request as ResponseEntity<Object>
//    @GetMapping("/id/{myId}")
//    public ResponseEntity<Object> getByIdR(@PathVariable long myId) {
//        Object obj = journalEntries.get(myId);
//        return obj != null ? ResponseEntity.ok(obj) : ResponseEntity.notFound().build();
//    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            boolean removed = journalEntryService.deleteById(myId, userName);
            if(removed)
                return new ResponseEntity<>("Requested Journal Entry is deleted !", HttpStatus.OK);
            else
                return new ResponseEntity<>("Requested Journal Entry is not found !", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> updateById(
            @PathVariable ObjectId myId,
            @RequestBody JournalEntry changedEntry
    ) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findByUserName(authentication.getName()).get();
            List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
            if(!collect.isEmpty()) {
                JournalEntry entry = journalEntryService.findById(myId).orElse(null);
                if (entry != null) {
                    entry.setTitle(changedEntry.getTitle() != null && !changedEntry.getTitle().isEmpty() ? changedEntry.getTitle() : entry.getTitle());
                    entry.setContent(changedEntry.getContent() != null && !changedEntry.getContent().isEmpty() ? changedEntry.getContent() : entry.getContent());
                    journalEntryService.saveEntry(entry);
                    return new ResponseEntity<>(entry, HttpStatus.CREATED);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
