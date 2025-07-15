package com.journal.backend.services;

import com.journal.backend.dto.CollectionDTO;
import com.journal.backend.dto.ReturnJournalDTO;
import com.journal.backend.entity.Collection;
import com.journal.backend.entity.JournalEntry;
import com.journal.backend.entity.User;
import com.journal.backend.repository.CollectionRepo;
import com.journal.backend.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService {
    private final CollectionRepo collectionRepo;
    private final UserService userService;
    private final JournalEntryRepository journalRepo;

    public Collection createCollection(Collection collection, String username) {
        if(collection==null) throw new IllegalArgumentException("Collection cannot be null.");
        User user = validateUser(username);
        boolean exists = collectionRepo.existsByUserAndTitle(user.getId(), collection.getTitle());
        if (exists) throw new IllegalArgumentException("Collection name should not be same.");

        collection.setUser(user.getId());
        return collectionRepo.save(collection);
    }

    public User validateUser(String username) {
        User user=userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No username found for: " + username);
        }
        return user;
    }

    public List<CollectionDTO> getCollections(String username) {
        User user=validateUser(username);
        List<Collection> collections=collectionRepo.findByUser(user.getId());
        return collections.stream().map(collection -> {
            List<ReturnJournalDTO> previewEntries = journalRepo
                    .findTop2ByUserAndCollectionIdOrderByCreatedAtDesc(user.getId(), collection.getId())
                    .stream().map(ReturnJournalDTO::fromEntity).toList();

            return CollectionDTO.builder()
                    .id(collection.getId().toHexString())
                    .title(collection.getTitle())
                    .description(collection.getDescription())
                    .entries(previewEntries)
                    .user(collection.getUser().toHexString())
                    .build();
        }).toList();
    }

    public Collection getCollectionById(String username, String id) {
        User user = validateUser(username);
        ObjectId collectionId;

        try {
            collectionId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid collection ID format.");
        }

        return collectionRepo.findByIdAndUser(collectionId, user.getId())
                .orElseThrow(() -> new RuntimeException("Collection not found or access denied."));
    }

    public void deleteCollection(UserDetails userDetails, String collectionId) {
        User user = validateUser(userDetails.getUsername());
        Collection collection=collectionRepo.findByIdAndUser(new ObjectId(collectionId), user.getId()).orElse(null);
        if(collection==null) throw new IllegalArgumentException("Invalid collection ID or access.");
        journalRepo.deleteByCollectionId(new ObjectId(collectionId));
        collectionRepo.delete(collection);
    }
}