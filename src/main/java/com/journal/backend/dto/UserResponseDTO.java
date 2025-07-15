package com.journal.backend.dto;

import com.journal.backend.entity.JournalEntry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String id;
    private String username;
    private String email;
    private List<JournalEntry> journalEntries;
}