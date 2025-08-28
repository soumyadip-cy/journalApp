package com.soumyadip_cy.journalApp.service;

import com.soumyadip_cy.journalApp.api.response.CityResponse;
import com.soumyadip_cy.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {

    @Value("${weather.api-key}")
    private String API_KEY;
    String kolkataKey = "206690";
    private static final String CITY_API = "http://dataservice.accuweather.com/locations/v1/cities/search?apikey=%s&q=%s";
    private static final String WEATHER_API = "https://dataservice.accuweather.com/currentconditions/v1/%s?apikey=%s";

    private final RestTemplate restTemplate;

    @Autowired
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getTemperature(String city) {
        String finalCityAPI = String.format(CITY_API, API_KEY , city);
        CityResponse[] cityResponse = restTemplate.exchange(finalCityAPI, HttpMethod.GET, null, CityResponse[].class).getBody();
        String cityCode;
        if(cityResponse != null) {
            cityCode = cityResponse[0].getKey();
            String finalWeatherAPI = String.format(WEATHER_API, cityCode, API_KEY);
            WeatherResponse[] weatherResponse = restTemplate.exchange(finalWeatherAPI, HttpMethod.GET, null, WeatherResponse[].class).getBody();
            if(weatherResponse != null) {
                String tempValue = String.valueOf(weatherResponse[0].getTemperature().getMetric().getValue());
                String unit = weatherResponse[0].getTemperature().getMetric().getUnit();
                return tempValue.concat(" ").concat(unit);
            }
        }
        
        return null;
    }
}
