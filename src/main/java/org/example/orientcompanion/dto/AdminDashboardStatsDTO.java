package org.example.orientcompanion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardStatsDTO implements Serializable {

    private OverviewStats overview;
    private CatalogStats catalog;
    private MentorshipStats mentorship;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OverviewStats implements Serializable {
        private long totalStudents;
        private long completedAssessments;
        private double completionRate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CatalogStats implements Serializable {
        private long totalSchools;
        private long publicSchools;
        private long privateSchools;
        private long totalFields;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MentorshipStats implements Serializable {
        private long pendingSessions;
        private long confirmedSessions;
        private long completedSessions;
        private long activeMentors;
    }
}