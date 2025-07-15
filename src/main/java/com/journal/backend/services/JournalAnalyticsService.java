package com.journal.backend.services;

import com.journal.backend.dto.DailyMoodStatsDTO;
import com.journal.backend.dto.DashboardAnalyticsResponseDTO;
import com.journal.backend.dto.SummaryStatsDTO;
import com.journal.backend.entity.JournalEntry;
import com.journal.backend.entity.User;
import com.journal.backend.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JournalAnalyticsService {
    private final CollectionService collectionService;
    private final JournalEntryRepository journalEntryRepository;
    private final MongoTemplate mongoTemplate;

    public DashboardAnalyticsResponseDTO getDashboardAnalytics(String username, int days) {
        User user = collectionService.validateUser(username);
        LocalDate fromDate = LocalDate.now().minusDays(days - 1);

        List<JournalEntry> entries = journalEntryRepository
                .findByUserAndDateAfter(user.getId(), fromDate.atStartOfDay());

        long totalEntries = entries.size();

        if (totalEntries == 0) {
            return new DashboardAnalyticsResponseDTO(
                    new SummaryStatsDTO(0, 0, 0, "unknown"),
                    new ArrayList<>(),
                    0
            );
        }

        Map<LocalDate, List<JournalEntry>> groupedByDate = entries.stream()
                .collect(Collectors.groupingBy(entry -> entry.getDate().toLocalDate()));

        List<DailyMoodStatsDTO> timeline = groupedByDate.entrySet().stream()
                .map(e -> new DailyMoodStatsDTO(
                        e.getKey().toString(),
                        e.getValue().stream().mapToInt(JournalEntry::getMoodScore).average().orElse(0),
                        e.getValue().size()
                ))
                .sorted(Comparator.comparing(DailyMoodStatsDTO::getDate))
                .collect(Collectors.toList());

        double dailyAverage = (double) totalEntries / days;
        double averageMoodScore = entries.stream().mapToInt(JournalEntry::getMoodScore).average().orElse(0);

        String mostFrequentMood = entries.stream()
                .collect(Collectors.groupingBy(JournalEntry::getMood, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("unknown");

        SummaryStatsDTO stats = new SummaryStatsDTO(totalEntries, dailyAverage, averageMoodScore, mostFrequentMood);

        return new DashboardAnalyticsResponseDTO(stats, timeline, totalEntries);
    }


}