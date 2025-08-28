package com.soumyadip_cy.journalApp.controller;

import com.soumyadip_cy.journalApp.api.response.WeatherResponse;
import com.soumyadip_cy.journalApp.dto.UserCreateDTO;
import com.soumyadip_cy.journalApp.dto.UserDTO;
import com.soumyadip_cy.journalApp.entity.User;
import com.soumyadip_cy.journalApp.mapper.UserMapper;
import com.soumyadip_cy.journalApp.service.TextToSpeechService;
import com.soumyadip_cy.journalApp.service.UserService;
import com.soumyadip_cy.journalApp.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final WeatherService weatherService;
    private final TextToSpeechService textToSpeechService;

    //Should be transferred to admin
//    @GetMapping
//    public ResponseEntity<List<User>> getAll() {
//        try {
//            List<User> users = userService.getAll();
//            if(users !=null && !users.isEmpty())
//                return new ResponseEntity<>(users, HttpStatus.OK);
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

    //Should be transferred to admin
//    @GetMapping("/{myUserName}")
//    public ResponseEntity<User> getByUserName(@PathVariable String myUserName) {
//        try {
//            Optional<User> user = userService.findByUserName(myUserName);
//            if(user.isPresent())
//                return new ResponseEntity<>(user.get(), HttpStatus.OK);
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }


    @PutMapping
    public ResponseEntity<UserDTO> updateById(@RequestBody UserCreateDTO user) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String myUserName = authentication.getName();
            Optional<User> existingUser = userService.findByUserName(myUserName);
            if(existingUser.isPresent()) {
                User newUser = existingUser.get();
                //The password is stored in the encrypted format, and when the previous password is saved again,
                //then the encrypted password is encrypted again while saving. In order to prevent that, when
                //an empty password is given or the password remains unchanged, the 'user' object saved.
                if(user.password().isEmpty()) {
                    newUser.setUserName(!user.userName().isEmpty() ? user.userName() : newUser.getUserName());
                    userService.saveUser(UserMapper.INSTANCE.toUser(user));
                } else {
                    newUser.setUserName(!user.userName().isEmpty() ? user.userName() : newUser.getUserName());
                    newUser.setPassword(!user.password().isEmpty() || !user.password().isBlank() ? user.password() : newUser.getPassword());
                    userService.createUser(newUser);
                }
                UserDTO userDTO = UserMapper.INSTANCE.toUserDTO(newUser);
                log.info("User information edited !");
                return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
            }
            log.info("User not found while editing !");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred while editing user !",e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteByUserName() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String myUserName = authentication.getName();
            userService.deleteByUserName(myUserName);
            log.info("User is deleted !");
            return new ResponseEntity<>("User is deleted !", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while deleting user !",e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private String getTemperature(String city) {
        String greeting = " Temperature in "+city+" is: ";
        try {
            String temperature = weatherService.getTemperature(city);
            return greeting.concat(temperature+" ");
        } catch (Exception e) {
            log.error("Exception occurred while getting temperature !", e);
            return "";
        }
    }

    @PostMapping()
    public ResponseEntity<String> greeting(@RequestBody Map<String, String> requestPayload) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String city = requestPayload.get("city");
            log.debug("City: "+city);
            String greeting = getTemperature(city);
            log.info("Greeting the user !");
            return new ResponseEntity<>("Hi, " + authentication.getName() + "." + greeting , HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while greeting the user !", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Index starts from 1. This is the index of the journal entry, and not the object ID.
    @GetMapping("/read-journal-entry/{entryIndex}")
    public ResponseEntity<Resource> getAudio(@PathVariable int entryIndex) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Optional<byte[]> audioBytesOptional = textToSpeechService.convertToSpeech(entryIndex, username);
            if(audioBytesOptional.isPresent()) {
                byte[] audioBytes = audioBytesOptional.get();
                Resource resource = new ByteArrayResource(audioBytes);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.valueOf("audio/mpeg"));
                httpHeaders.setContentDispositionFormData("attachment", "audio.mp3");
                return ResponseEntity.ok()
                        .headers(httpHeaders)
                        .contentLength(resource.contentLength())
                        .body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Exception occurred while getting voice !", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
