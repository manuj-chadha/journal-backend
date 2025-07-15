package com.journal.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisServiceTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testEmail(){
        redisTemplate.opsForValue().set("email", "manujchadha7777@gmail.com");
        Object salary = redisTemplate.opsForValue().get("salary");
        int add=1;
    }
}