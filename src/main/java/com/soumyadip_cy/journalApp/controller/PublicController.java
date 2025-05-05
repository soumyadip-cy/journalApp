package com.soumyadip_cy.journalApp.controller;

import com.soumyadip_cy.journalApp.entity.User;
import com.soumyadip_cy.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/get-all-users")
    public ResponseEntity<List<User>> getAll() {
        try {
            List<User> users = userService.getAll();
            if(users !=null && !users.isEmpty())
                return new ResponseEntity<>(users, HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User myUser) {
        try {
            userService.createUser(myUser);
            return new ResponseEntity<>(myUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Ok !!!";
    }

    @RequestMapping(value = "/health-check-fail", method = RequestMethod.GET)
    public String healthCheckFail() {
        return "Not Ok !!!";
    }
}
