package com.journal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardAnalyticsResponseDTO {

    private SummaryStatsDTO stats;
    private List<DailyMoodStatsDTO> timeline;
    private long totalEntries;
}