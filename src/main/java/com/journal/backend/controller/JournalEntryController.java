package com.journal.backend.controller;

import com.journal.backend.dto.ApiResponse;
import com.journal.backend.dto.JournalEntryDTO;
import com.journal.backend.dto.ReturnJournalDTO;
import com.journal.backend.entity.JournalEntry;
import com.journal.backend.services.JournalEntryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
@Slf4j
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    // Get all journal entries
//    @GetMapping
//    public ResponseEntity<?> getAllJournalEntriesOfUser(@AuthenticationPrincipal UserDetails userDetails) {
//        List<JournalEntry> entries = journalEntryService.getJournalEntries(userDetails.getUsername(), collectionId);
//        if (entries != null ) {
//            return new ResponseEntity<>(entries, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }

    // Create a new journal entry
    @PostMapping
    public ResponseEntity<ApiResponse<ReturnJournalDTO>> createEntry(
            @Valid @RequestBody JournalEntryDTO entryDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String username = userDetails.getUsername();
            JournalEntry createdEntry = journalEntryService.createEntry(entryDto, username);

            ReturnJournalDTO responseDto = ReturnJournalDTO.fromEntity(createdEntry); // Convert entity to DTO

            ApiResponse<ReturnJournalDTO> response = ApiResponse.<ReturnJournalDTO>builder()
                    .success(true)
                    .message("Journal entry created successfully")
                    .data(responseDto)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (UsernameNotFoundException ex) {
            ApiResponse<ReturnJournalDTO> error = ApiResponse.<ReturnJournalDTO>builder()
                    .success(false)
                    .message("User not found: " + ex.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

        } catch (IllegalArgumentException ex) {
            ApiResponse<ReturnJournalDTO> error = ApiResponse.<ReturnJournalDTO>builder()
                    .success(false)
                    .message("Invalid request: " + ex.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        } catch (Exception ex) {
            log.error("Error while creating journal entry", ex);
            ApiResponse<ReturnJournalDTO> error = ApiResponse.<ReturnJournalDTO>builder()
                    .success(false)
                    .message("Something went wrong, please try again later.")
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }



    // Get journal entry by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReturnJournalDTO>> getJournalEntryById(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            JournalEntry entry = journalEntryService.findById(new ObjectId(id), userDetails.getUsername());
            if (entry == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.<ReturnJournalDTO>builder()
                                .success(false)
                                .message("Journal entry not found")
                                .data(null)
                                .build());
            }

            return ResponseEntity.ok(
                    ApiResponse.<ReturnJournalDTO>builder()
                            .success(true)
                            .message("Journal entry fetched successfully")
                            .data(ReturnJournalDTO.fromEntity(entry))
                            .build()
            );

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<ReturnJournalDTO>builder()
                            .success(false)
                            .message("Invalid request: " + ex.getMessage())
                            .data(null)
                            .build());

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<ReturnJournalDTO>builder()
                            .success(false)
                            .message("Something went wrong")
                            .data(null)
                            .build());
        }
    }


    // Update journal entry
    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse<ReturnJournalDTO>> updateJournalEntry(
            @Valid @RequestBody JournalEntryDTO entryDTO,
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable ObjectId id) {

        try {
            JournalEntry journalEntry = journalEntryService.findById(id, userDetails.getUsername());

            if (entryDTO.getTitle() != null && !entryDTO.getTitle().isBlank()) {
                journalEntry.setTitle(entryDTO.getTitle());
            }

            if (entryDTO.getContent() != null && !entryDTO.getContent().isBlank()) {
                journalEntry.setContent(entryDTO.getContent());
            }

            if (entryDTO.getMood() != null && !entryDTO.getMood().isBlank()) {
                journalEntry.setMood(entryDTO.getMood());
            }

            JournalEntry updatedEntry = journalEntryService.saveEntry(journalEntry);


            return ResponseEntity.ok(
                    ApiResponse.<ReturnJournalDTO>builder()
                            .success(true)
                            .message("Journal entry updated successfully")
                            .data(ReturnJournalDTO.fromEntity(updatedEntry))
                            .build()
            );

        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<ReturnJournalDTO>builder()
                            .success(false)
                            .message("User not found: " + ex.getMessage())
                            .data(null)
                            .build()
            );

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<ReturnJournalDTO>builder()
                            .success(false)
                            .message("Invalid request: " + ex.getMessage())
                            .data(null)
                            .build()
            );

        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<ReturnJournalDTO>builder()
                            .success(false)
                            .message("Something went wrong while updating entry")
                            .data(null)
                            .build()
            );
        }
    }



    // Delete journal entry by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJournalEntryById(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {
            journalEntryService.deleteJournalEntry(new ObjectId(id), userDetails.getUsername());

            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.<Void>builder()
                            .success(true)
                            .message("Journal entry deleted successfully.")
                            .data(null)
                            .build());

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<Void>builder()
                            .success(false)
                            .message(ex.getMessage())
                            .data(null)
                            .build()
            );

        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.<Void>builder()
                            .success(false)
                            .message("Unauthorized: " + ex.getMessage())
                            .data(null)
                            .build()
            );

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<Void>builder()
                            .success(false)
                            .message("Something went wrong.")
                            .data(null)
                            .build()
            );
        }
    }

}