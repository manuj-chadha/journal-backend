package com.journal.backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class WeatherResponse {
    public Current current;

    @Getter
    @Setter
    public class Current {
        private int temperature;
        @JsonProperty("weather_description")
        private ArrayList<String> weatherDescriptions;
        private int feelslike;
    }

}