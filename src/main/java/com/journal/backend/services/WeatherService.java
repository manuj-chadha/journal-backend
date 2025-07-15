package com.journal.backend.services;

import com.journal.backend.controller.AppCache;
import com.journal.backend.entity.User;
import com.journal.backend.entity.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${apiKey}")
    private String apiKey;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;


    @Autowired
    private RestTemplate restTemplate;
//    User user= User.builder().username("manuj").password("manuj123").build();
//    HttpHeaders headers=new HttpHeaders();
//    headers.set("key", "value");
//    HttpEntity<User> httpEntity=new HttpEntity<>(user);

    public WeatherResponse getWeather(String city){
        WeatherResponse response = redisService.get("weather_of_" + city, WeatherResponse.class);
        if(response!=null) return response;
        else {
            String rawUrl = appCache.map.get(AppCache.keys.WEATHER_API_URL.toString());
            String url=appCache.map.get(AppCache.keys.WEATHER_API_URL.toString()).replace("<city>", city).replace("<apiKey>", apiKey);
            ResponseEntity<WeatherResponse> weatherData=restTemplate.exchange(url, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = weatherData.getBody();
            if(body!=null){
                redisService.set("weather_of_" + city, body, 300L);
            }
            return body;
        }

    }
}