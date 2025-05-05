package com.soumyadip_cy.journalApp.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

//This is done so that a JournalEntry instance will be treated as a new Mongo document.
//This annotation tells Spring Boot to treat this as an entity mapped to a Mongo collection.
//If a name for a collection is mentioned, then that collection will be searched in the MongoDB,
//otherwise the name of the class will be considered as the name of the collection.
@Document(collection = "journal_entries")
@Data
public class JournalEntry {

    //This annotation tells Spring Boot to consider this as primary key of the Mongo DB document.
    @Id
    private ObjectId id;

    private String title;

    private String content;

    private LocalDateTime date;

}
