package com.soumyadip_cy.journalApp.service;

import com.soumyadip_cy.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUserName() {
        Assertions.assertNotNull(userService.findByUserName("batman").get());
    }
}
