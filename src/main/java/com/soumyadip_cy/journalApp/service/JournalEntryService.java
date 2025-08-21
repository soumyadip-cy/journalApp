package com.soumyadip_cy.journalApp.service;

import com.soumyadip_cy.journalApp.entity.JournalEntry;
import com.soumyadip_cy.journalApp.entity.User;
import com.soumyadip_cy.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public static final Logger logger = LoggerFactory.getLogger(JournalEntryService.class);

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
        journalEntry.setDate(LocalDateTime.now());
        User user = userService.findByUserName(userName).orElse(null);
        JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
        if(user!=null) {
            user.getJournalEntries().add(savedEntry);
        //This is an incorrect line meant for checking how transactions happen in Spring Data MongoDB
//            user.setUserName(null);
            userService.saveUser(user);
            }
        } catch (Exception e) {
            logger.error("Exception occurred while saving a journal entry: ",e);
            throw new RuntimeException("An error has occurred while saving a journal entry");
        }
        logger.info("A new journal entry has been saved !");
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
        logger.info("A journal entry edit is successful !");
    }

    public List<JournalEntry> getAll() {
        try {
            List<JournalEntry> journalEntries = journalEntryRepository.findAll();
            logger.info("Journal entry access successful !");
            return journalEntries;
        } catch (Exception e) {
            logger.error("Exception occurred while fetching journal entries !",e);
            return Collections.emptyList();
        }
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        Optional<JournalEntry> journalEntry = journalEntryRepository.findById(id);
        if(journalEntry.isPresent())
            logger.info("Journal entry found");
        else
            logger.info("Journal entry not found !");
        return journalEntry;
    }

    public boolean deleteById(ObjectId id, String userName) {
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName).orElse(null);
            //Runs a loop that checks for a document with the given id and deletes it.
            if(user!=null) {
                removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e) {
            logger.error("Exception occurred while deleting journal entry !",e);
            throw new RuntimeException("An error has occurred while deleting the Journal Entry !");
        }
        logger.info(removed ? "Journal entries deleted !" : "No Journal entries deleted !");
        return removed;
    }
}
