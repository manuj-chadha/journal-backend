package com.journal.backend.entity;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "collections")
@Builder
public class Collection {
    private ObjectId id;
    private String title;
    private String description;
    private List<JournalEntry> entries=new ArrayList<>();
    private ObjectId user;
}