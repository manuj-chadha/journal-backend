package com.journal.backend.dto;

import com.journal.backend.entity.Collection;
import com.journal.backend.entity.JournalEntry;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CollectionDTO {
    private String id;
    private String title;
    private String description;
    private List<ReturnJournalDTO> entries;
    private String user;
    public static CollectionDTO fromCollection(Collection collection){
        return CollectionDTO.builder()
                .id(collection.getId().toHexString())
                .title(collection.getTitle())
                .description(collection.getDescription())
                .entries(collection.getEntries() == null ? List.of() :
                        collection.getEntries()
                                .stream().map(ReturnJournalDTO::fromEntity)
                                .collect(Collectors.toList()))
                .user(collection.getUser().toHexString())
                .build();
    }
}