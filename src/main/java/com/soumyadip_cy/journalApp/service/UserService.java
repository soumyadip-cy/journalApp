package com.soumyadip_cy.journalApp.service;

import com.soumyadip_cy.journalApp.entity.User;
import com.soumyadip_cy.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //This is used during creation/modification
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
    }

    //This is used to create an Admin user
    public void createAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER", "ADMIN"));
        userRepository.save(user);
    }

    //This will be used while saving a new journal entry, as the ID of the newly created journal entry will also be
    //saved in the user
    public void saveUser(User user) {
        userRepository.save(user);
    }

    //This is used to get all the users
    public List<User> getAll() {
        return userRepository.findAll();
    }

    //This is used to get the user by their userName
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    //This is used to delete the user by their userName
    public void deleteByUserName(String userName) {
        userRepository.deleteByUserName(userName);
    }
}
