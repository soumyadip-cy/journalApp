package com.soumyadip_cy.journalApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TextToSpeechEntityDTO {

    @JsonProperty("text")
    private String text;

    @JsonProperty("model_id")
    private String modelId;

}
