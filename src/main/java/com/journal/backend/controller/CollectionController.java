package com.journal.backend.controller;

import com.journal.backend.dto.ApiResponse;
import com.journal.backend.dto.CollectionDTO;
import com.journal.backend.dto.ReturnJournalDTO;
import com.journal.backend.entity.Collection;
import com.journal.backend.entity.JournalEntry;
import com.journal.backend.entity.User;
import com.journal.backend.services.CollectionService;
import com.journal.backend.services.JournalEntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
@Slf4j
public class CollectionController {

    private final CollectionService collectionService;
    private final JournalEntryService journalEntryService;


    @PostMapping
    public ResponseEntity<ApiResponse<CollectionDTO>> createCollection(
            @Valid @RequestBody Collection collection,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            log.info(String.valueOf(collection));
            Collection savedCollection = collectionService.createCollection(collection, userDetails.getUsername());

            ApiResponse<CollectionDTO> response = ApiResponse.<CollectionDTO>builder()
                    .success(true)
                    .message("Collection created successfully")
                    .data(CollectionDTO.fromCollection(savedCollection))
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (UsernameNotFoundException ex) {
            ApiResponse<CollectionDTO> error = ApiResponse.<CollectionDTO>builder()
                    .success(false)
                    .message("User not found: " + ex.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

        } catch (IllegalArgumentException ex) {
            ApiResponse<CollectionDTO> error = ApiResponse.<CollectionDTO>builder()
                    .success(false)
                    .message("Invalid request: " + ex.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        } catch (Exception ex) {
            log.error(ex.getMessage());
            ApiResponse<CollectionDTO> error = ApiResponse.<CollectionDTO>builder()
                    .success(false)
                    .message("Something went wrong")
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CollectionDTO>>> getCollections(
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            List<CollectionDTO> collect = collectionService.getCollections(userDetails.getUsername());
            return ResponseEntity.ok(
                    ApiResponse.<List<CollectionDTO>>builder()
                            .success(true)
                            .message("Collections fetched successfully")
                            .data(collect)
                            .build()
            );

        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<List<CollectionDTO>>builder()
                            .success(false)
                            .message("User not found: " + ex.getMessage())
                            .data(null)
                            .build()
            );

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<List<CollectionDTO>>builder()
                            .success(false)
                            .message("Invalid request: " + ex.getMessage())
                            .data(null)
                            .build()
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<List<CollectionDTO>>builder()
                            .success(false)
                            .message("Something went wrong")
                            .data(null)
                            .build()
            );
        }
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<ApiResponse<Map<String, ?>>> getCollectionById(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable String collectionId) {

        try {
            Collection collection = collectionService.getCollectionById(userDetails.getUsername(), collectionId);
            List<JournalEntry> entries = journalEntryService.getJournalEntries(userDetails.getUsername(), new ObjectId(collectionId));
            return ResponseEntity.ok(
                    ApiResponse.<Map<String, ?>>builder()
                            .success(true)
                            .message("Collection fetched successfully")
                            .data(Map.of("collection", CollectionDTO.fromCollection(collection), "entries", entries.stream().map(ReturnJournalDTO::fromEntity).collect(Collectors.toList())))
                            .build()
            );

        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Map<String, ?>>builder()
                            .success(false)
                            .message("User not found: " + ex.getMessage())
                            .data(null)
                            .build()
            );

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<Map<String, ?>>builder()
                            .success(false)
                            .message("Invalid request: " + ex.getMessage())
                            .data(null)
                            .build()
            );

        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Map<String, ?>>builder()
                            .success(false)
                            .message("Something went wrong")
                            .data(null)
                            .build()
            );
        }
    }

    @DeleteMapping("/{collectionId}")
    public ResponseEntity<ApiResponse<?>> deleteCollectionById(
            @AuthenticationPrincipal UserDetails userDetails, @PathVariable String collectionId) {

        try {
            collectionService.deleteCollection(userDetails, collectionId);
            return ResponseEntity.ok(
                    ApiResponse.<Map<String, ?>>builder()
                            .success(true)
                            .message("Collection deleted successfully")
                            .data(null)
                            .build()
            );

        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<Map<String, ?>>builder()
                            .success(false)
                            .message("User not found: " + ex.getMessage())
                            .data(null)
                            .build()
            );

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<Map<String, ?>>builder()
                            .success(false)
                            .message("Invalid request: " + ex.getMessage())
                            .data(null)
                            .build()
            );

        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Map<String, ?>>builder()
                            .success(false)
                            .message("Something went wrong")
                            .data(null)
                            .build()
            );
        }
    }
}