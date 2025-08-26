package com.soumyadip_cy.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CityResponse {

    @JsonProperty("Key")
    private String key;
    @JsonProperty("EnglishName")
    private String englishName;

//    private Root root;
//
//    @Getter
//    public class Root{
//        @JsonProperty("Key")
//        private String key;
//        @JsonProperty("EnglishName")
//        private String englishName;
//    }
}
