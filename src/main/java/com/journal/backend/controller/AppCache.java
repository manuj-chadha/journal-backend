package com.journal.backend.controller;

import com.journal.backend.entity.ConfigKey;
import com.journal.backend.repository.ConfigRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class AppCache {

    @Autowired
    private ConfigRepository configRepository;

    public enum keys {
        WEATHER_API_URL;
    }

    public HashMap<String, String> map;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            map=new HashMap<>();
            List<ConfigKey> pairs = configRepository.findAll();
            for (ConfigKey configKey : pairs) {
                map.put(configKey.getKey(), configKey.getValue());
            }
            System.out.println("AppCache initialized on app ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}