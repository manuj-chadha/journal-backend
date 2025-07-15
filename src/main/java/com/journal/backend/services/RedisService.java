package com.journal.backend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.journal.backend.entity.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String city, Class<T> entityClass){
        try{
            Object o=redisTemplate.opsForValue().get(city);
            ObjectMapper objectMapper=new ObjectMapper();
            return objectMapper.readValue(o.toString(), entityClass);
        } catch(Exception e){
            log.error(e.getMessage());
            return null;
        }
    }public void set(String city, Object o, Long ttl){
        try{
            ObjectMapper objectMapper=new ObjectMapper();
            String string=objectMapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(city, string, ttl, TimeUnit.SECONDS);
        } catch(Exception e){
            log.error(e.getMessage());
        }
    }
}