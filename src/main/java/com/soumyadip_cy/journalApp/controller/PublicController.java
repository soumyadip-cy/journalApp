package com.soumyadip_cy.journalApp.controller;

import com.soumyadip_cy.journalApp.dto.UserCreateDTO;
import com.soumyadip_cy.journalApp.dto.UserDTO;
import com.soumyadip_cy.journalApp.entity.User;
import com.soumyadip_cy.journalApp.mapper.UserMapper;
import com.soumyadip_cy.journalApp.service.UserService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@Slf4j
@RequiredArgsConstructor // This is for autowiring.
public class PublicController {

    private final UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<UserCreateDTO> createUser(@RequestBody UserCreateDTO myUser) {
        try {
            userService.createUser(UserMapper.INSTANCE.toUser(myUser));
            log.info("User created !");
            return new ResponseEntity<>(myUser, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Exception occurred while creating a user !",e);
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
