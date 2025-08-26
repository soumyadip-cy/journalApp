package com.soumyadip_cy.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WeatherResponse {

    @JsonProperty("Temperature")
    private Temperature temperature;

    /**
     * Jackson, the JSON deserializer requires the Metric and Temperature class, but it cannot create one in scenarios where
     * it is a nested inner class, so we need to use the static keyword so that it is not bound to the outer class
     * i.e., the inner class can be used. Another way is to create a Temperature object inside the outer class.
     */
    @Getter
    @NoArgsConstructor
    public class Temperature{

        @JsonProperty("Metric")
        private Metric metric;

        @Getter
        @NoArgsConstructor
        public class Metric{
            @JsonProperty("Value")
            private double value;
            @JsonProperty("Unit")
            private String unit;
        }
    }
}
