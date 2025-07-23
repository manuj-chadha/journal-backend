package com.journal.backend.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @NonNull
    private String username;
    @NonNull
    private String password;
    private String email;
    private boolean sentiment;
    private String recaptchaToken;
    @DBRef
    private List<Collection> collections=new ArrayList<>();
    @DBRef
    private List<JournalEntry> journalEntries=new ArrayList<>();
    private List<String> roles = new ArrayList<>();

}