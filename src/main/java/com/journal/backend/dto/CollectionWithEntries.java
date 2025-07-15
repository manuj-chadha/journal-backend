package com.journal.backend.dto;

import com.journal.backend.entity.JournalEntry;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CollectionWithEntries {
    private String id;
    private String title;
    private String description;
    private List<JournalEntry> entries;
    private String user;
}