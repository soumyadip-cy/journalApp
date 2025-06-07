package com.soumyadip_cy.journalApp.service;

import com.soumyadip_cy.journalApp.entity.JournalEntry;
import com.soumyadip_cy.journalApp.entity.User;
import com.soumyadip_cy.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
        journalEntry.setDate(LocalDateTime.now());
        User user = userService.findByUserName(userName).get();
        JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(savedEntry);
        //This is an incorrect line meant for checking how transactions happen in Spring Data MongoDB
//        user.setUserName(null);
        userService.saveUser(user);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("An error has occurred while saving a Journal Entry");
        }
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    public boolean deleteById(ObjectId id, String userName) {
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName).get();
            //Runs a loop that checks for a document with the given id and deletes it.
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            userService.saveUser(user);
            journalEntryRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("An error has occurred while deleting the Journal Entry !");
        }
        return removed;
    }
}
