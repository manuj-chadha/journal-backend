package com.journal.backend.services;

import com.journal.backend.config.enums.Sentiment;
import com.journal.backend.controller.AppCache;
import com.journal.backend.entity.JournalEntry;
import com.journal.backend.entity.SentimentData;
import com.journal.backend.entity.User;
import com.journal.backend.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserScheduler {
    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AppCache appCache;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void sendEmailtoUsersWithSA(){
        List<User> usersBySA = userRepository.findUsersBySA();
        for(User user: usersBySA){
            List<JournalEntry> journalEntries=user.getJournalEntries();
            if(journalEntries.isEmpty()) return;
            List<Sentiment> sentiments= journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minusDays(7))).map(x -> x.getSentiment()).toList();
            HashMap<Sentiment, Integer> map=new HashMap<>();
            for(Sentiment sentiment: sentiments){
                if(sentiment!=null) map.put(sentiment, map.getOrDefault(sentiment, 0)+1);
            }
            Sentiment mostFrequentSentiment=null;
            int maxCount=0;
            for(Map.Entry<Sentiment, Integer> entry: map.entrySet()){
                if(entry.getValue()>maxCount){
                    maxCount=entry.getValue();
                    mostFrequentSentiment=entry.getKey();
                }
            }
            if(mostFrequentSentiment!=null){
                SentimentData sentimentData=SentimentData.builder().email(user.getEmail()).sentiment("Your sentiment in past 7 days: "+ mostFrequentSentiment.toString()).build();
                kafkaTemplate.send("sentiment-analysis", sentimentData.getEmail(), sentimentData);
            }


        }
    }
    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache(){
        appCache.init();
    }

}