package com.soumyadip_cy.journalApp.service;

import com.soumyadip_cy.journalApp.entity.User;
import com.soumyadip_cy.journalApp.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("UserServiceTests")
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Disabled
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

    @Disabled
    @Test
    public void testFindByUserName() {
        String userName = "lois lane";
        Optional<User> user = userService.findByUserName(userName);
        Assertions.assertNull(user.isPresent() ? user.get() : null, "Failed for: "+userName);
    }

    @Order(1)
    @DisplayName("Testing createUser()")
    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    public void testCreateUser(User user) {
        Assertions.assertTrue(userService.createUser(user));
    }

    @Order(2)
    @DisplayName("Negative Testing createUser()")
    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    public void negativeTestDuplicateCreateUser(User user) {
        Assertions.assertFalse(userService.createUser(user));
    }

    @Order(3)
    @DisplayName("Testing deleteUser()")
    @ParameterizedTest
    @ValueSource(strings = {
            "User1",
            "User2"
    })
    public void testDeleteUser(String userName) {
        Assertions.assertTrue(userService.deleteByUserName(userName));
    }

}
