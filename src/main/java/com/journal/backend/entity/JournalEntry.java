package com.journal.backend.entity;

import com.journal.backend.config.enums.Sentiment;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@Builder
@Document(collection = "entries")
public class JournalEntry {
    @Id
    private ObjectId id;
    private String title;
    private String content;
    private LocalDateTime date;
    private String mood;
    private int moodScore;
    @Indexed
    private ObjectId collectionId;
    @Indexed
    private ObjectId user;
    private Sentiment sentiment;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;

}