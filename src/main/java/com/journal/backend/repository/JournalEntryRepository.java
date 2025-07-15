package com.journal.backend.repository;

import com.journal.backend.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, ObjectId> {

    public List<JournalEntry> findByUserAndCollectionId(ObjectId user, ObjectId collectionId);

    List<JournalEntry> findTop2ByUserAndCollectionIdOrderByCreatedAtDesc(ObjectId id, ObjectId id1);

    void deleteByCollectionId(ObjectId objectId);

    List<JournalEntry> findByUserAndDateAfter(ObjectId id, LocalDateTime localDateTime);
}