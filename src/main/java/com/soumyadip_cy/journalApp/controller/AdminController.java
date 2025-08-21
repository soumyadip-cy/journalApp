package com.soumyadip_cy.journalApp.controller;

import com.soumyadip_cy.journalApp.entity.User;
import com.soumyadip_cy.journalApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> userList = userService.getAll();
            if (userList != null && !userList.isEmpty()) {
                return new ResponseEntity<>(userList, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error in fetching users !", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("Fetched all users !");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/add-admin")
    public ResponseEntity<?> createAdmin(@RequestBody User user) {
        try {
            userService.createAdmin(user);
            log.info("Admin added !");
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Exception in admin creation !",e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
