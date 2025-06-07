package com.soumyadip_cy.journalApp.service;

import com.soumyadip_cy.journalApp.entity.User;
import com.soumyadip_cy.journalApp.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

//    AutoCloseable closeableMockInitialization;
//
//    @BeforeEach
//    void setUp() {
////        Deprecated way to initialize mocks.
////        MockitoAnnotations.initMocks(this);
//        closeableMockInitialization = MockitoAnnotations.openMocks(this);
//    }
//
//    @AfterEach
//    void tearDown() {
//        try {
//            closeableMockInitialization.close();
//        } catch (Exception e) {
//            System.out.println("Error while closing openMocks: "+e);
//        }
//    }

    @Test
    void loadUserByUsernameTest() {
        String mockUsername = "MockUser";
        String mockPassword = "MockPassword";
        when(userRepository.findByUserName(ArgumentMatchers.anyString()))
                .thenReturn(
                        Optional.of(User.builder()
                                .userName(mockUsername)
                                .password(mockPassword)
                                .roles(Arrays.asList("USER"))
                                .build())
                );
        UserDetails user = userDetailsService.loadUserByUsername(mockUsername);
        Assertions.assertNotNull(user, "User is null");
    }

}
