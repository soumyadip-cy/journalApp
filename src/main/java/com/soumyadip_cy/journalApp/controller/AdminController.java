package com.soumyadip_cy.journalApp.controller;

import com.soumyadip_cy.journalApp.dto.UserCreateDTO;
import com.soumyadip_cy.journalApp.dto.UserDTO;
import com.soumyadip_cy.journalApp.entity.User;
import com.soumyadip_cy.journalApp.mapper.UserMapper;
import com.soumyadip_cy.journalApp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDTO>> getAll() {
        try {
            List<UserDTO> users = userService.getAllUserDTO();
            if(users !=null && !users.isEmpty()) {
                log.info("Users found !");
                return new ResponseEntity<>(users, HttpStatus.OK);
            }
            log.info("User not found !");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred while fetching all users !",e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-user/{userName}")
    public ResponseEntity<UserDTO> getByUserName(@PathVariable String userName) {
        try {
            Optional<User> user = userService.findByUserName(userName);
            if(user.isPresent()) {
                UserDTO obtainedUser = UserMapper.INSTANCE.toUserDTO(user.get());
                return new ResponseEntity<>(obtainedUser, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add-admin")
    public ResponseEntity<UserDTO> createAdmin(@RequestBody UserCreateDTO user) {
        try {
            User newAdmin = UserMapper.INSTANCE.toUser(user);
            userService.createAdmin(newAdmin);
            log.info("Admin added !");
            return new ResponseEntity<>(UserMapper.INSTANCE.toUserDTO(newAdmin), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Exception in admin creation !",e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
