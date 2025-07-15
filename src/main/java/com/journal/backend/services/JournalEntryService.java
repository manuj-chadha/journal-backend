package com.journal.backend.services;

import com.journal.backend.dto.JournalEntryDTO;
import com.journal.backend.entity.Collection;
import com.journal.backend.entity.JournalEntry;
import com.journal.backend.entity.User;
import com.journal.backend.repository.CollectionRepo;
import com.journal.backend.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JournalEntryService {
    private final UserService userService;
    private final JournalEntryRepository journalEntryRepository;
    private final CollectionService collectionService;
    private final CollectionRepo collectionRepo;

    public JournalEntry createEntry(JournalEntryDTO entry, String username) {
        User user = collectionService.validateUser(username);

        ObjectId collectionId;
        if (entry.getCollectionId().isEmpty()) {
            Collection unorganized = collectionRepo.findCollectionByTitleAndUser("Unorganized", user.getId());
            if (unorganized == null) {
                unorganized = Collection.builder()
                        .title("Unorganized")
                        .user(user.getId())
                        .build();
                unorganized = collectionRepo.save(unorganized);
            }
            collectionId = unorganized.getId();
        } else {
            collectionId = new ObjectId(entry.getCollectionId());
        }

        JournalEntry journalEntry = JournalEntry.builder()
                .title(entry.getTitle())
                .content(entry.getContent())
                .date(LocalDateTime.now())
                .mood(entry.getMood())
                .collectionId(collectionId)
                .user(user.getId())
                .build();

        return journalEntryRepository.save(journalEntry);
    }



    public List<JournalEntry> getJournalEntries(String username, ObjectId collectionId) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User unauthorized.");
        }

        if (collectionId == null) {
            throw new IllegalArgumentException("Collection ID cannot be null.");
        }

        return journalEntryRepository.findByUserAndCollectionId(user.getId(), collectionId);
    }


    public JournalEntry findById(ObjectId id, String username) {
        User user = collectionService.validateUser(username);
        JournalEntry entry = journalEntryRepository.findById(id).orElse(null);
        if(entry==null) throw new IllegalArgumentException("Invalid journal ID provide.");
        if (entry.getUser() == null || !entry.getUser().equals(user.getId())) throw new UsernameNotFoundException("Unauthorized access to the journal");
        return entry;
    }
    @Transactional
    public void deleteJournalEntry(ObjectId id, String username) {
        User user = collectionService.validateUser(username);

        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Journal entry not found with id: " + id));

        if (!entry.getUser().equals(user.getId())) {
            throw new UsernameNotFoundException("Unauthorized access to this journal entry.");
        }
        journalEntryRepository.deleteById(id);
    }



    public JournalEntry saveEntry(JournalEntry journalEntry) {
        return journalEntryRepository.save(journalEntry);
    }
}