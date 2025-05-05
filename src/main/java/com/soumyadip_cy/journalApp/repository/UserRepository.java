package com.soumyadip_cy.journalApp.repository;

import com.soumyadip_cy.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findByUserName(String userName);
    void deleteByUserName(String userName);
}
