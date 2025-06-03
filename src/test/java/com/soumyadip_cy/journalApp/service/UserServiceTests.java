package com.soumyadip_cy.journalApp.service;

import com.soumyadip_cy.journalApp.entity.User;
import com.soumyadip_cy.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @ParameterizedTest
    @ValueSource(strings = {
            "batman",
            "superman",
            "wonderwoman",
    })
    public void testFindByUserNameMultiple(String userName) {
        Optional<User> user = userRepository.findByUserName(userName);
        Assertions.assertNotNull(user.orElse(null), "Failed for: "+userName);
    }

    @Test
    public void testFindByUserName() {
        String userName = "lois lane";
        Optional<User> user = userService.findByUserName(userName);
        Assertions.assertNull(user.isPresent() ? user.get() : null, "Failed for: "+userName);
    }

    

}
