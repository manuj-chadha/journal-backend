package com.journal.backend.services;

import com.journal.backend.entity.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "sentiment-analysis", groupId = "weekly-sentiment-group")
    public void consume(SentimentData sentimentData){
        sendEmail(sentimentData);
    }

    public void sendEmail(SentimentData sentimentData){
        emailService.sendEmail(sentimentData.getEmail(), "your sentiment", "tera  sentiment kuch dino se "+ sentimentData.getSentiment() + "saa thaa.");
    }
}