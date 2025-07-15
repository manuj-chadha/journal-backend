package com.journal.backend.dto;

import com.journal.backend.config.enums.Sentiment;
import com.journal.backend.entity.JournalEntry;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class ReturnJournalDTO {
    private String id;
    private String title;
    private String content;
    private LocalDateTime date;
    private String mood;
    private String collectionId;
    private String user;
    private Date createdAt;
    private Date updatedAt;

    public static ReturnJournalDTO fromEntity(JournalEntry journalEntry){
        return ReturnJournalDTO.builder()
                .id(journalEntry.getId().toHexString())
                .title(journalEntry.getTitle())
                .content(journalEntry.getContent())
                .date(journalEntry.getDate())
                .mood(journalEntry.getMood())
                .collectionId(journalEntry.getCollectionId().toHexString())
                .user(journalEntry.getUser().toHexString())
                .createdAt(journalEntry.getCreatedAt())
                .updatedAt(journalEntry.getUpdatedAt())
                .build();
    }
}