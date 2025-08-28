package com.soumyadip_cy.journalApp.service;

import com.soumyadip_cy.journalApp.dto.TextToSpeechEntityDTO;
import com.soumyadip_cy.journalApp.entity.JournalEntry;
import com.soumyadip_cy.journalApp.entity.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Data
@Slf4j
public class TextToSpeechService {

    @Value("${tts.api-key}")
    private String API_KEY;
    private static final String CREATE_SPEECH_API = "https://api.elevenlabs.io/v1/text-to-speech/%s?output_format=mp3_44100_128";

    private final RestTemplate restTemplate;
    private final UserService userService;
    private final JournalEntryService journalEntryService;

    public Optional<byte[]> convertToSpeech(int index, String username) {

        Optional<byte[]> voiceNote = Optional.empty();
        String voiceId = "JBFqnCBsd6RMkjVDRZzb";
        String modelId = "eleven_multilingual_v2";
        Optional<User> user = userService.findByUserName(username);

        if(user.isPresent()) {
            log.info("Username: "+user.get().getUserName());
            List<JournalEntry> journalEntries = user.get().getJournalEntries();
            Optional<JournalEntry> journalEntry = journalEntryService.findById(journalEntries.get(index-1).getId());
            if (journalEntry.isPresent()) {
                log.info("Journal entry found !");
                String finalAPI = String.format(CREATE_SPEECH_API, voiceId);
                TextToSpeechEntityDTO ttsDTO = new TextToSpeechEntityDTO();
                ttsDTO.setText(journalEntry.get().getContent());
                ttsDTO.setModelId(modelId);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.set("xi-api-key", API_KEY);
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<TextToSpeechEntityDTO> httpEntity = new HttpEntity<>(ttsDTO, httpHeaders);
                try {
                    voiceNote = Optional.ofNullable(restTemplate.exchange(finalAPI, HttpMethod.POST, httpEntity, byte[].class).getBody());
                    log.info("Audio from TextToSpeech API received ");
                } catch (Exception e) {
                    log.error("Exception occurred while getting response from TextToSpeech API !");
                }
            }
        }

        return voiceNote;
    }
}
