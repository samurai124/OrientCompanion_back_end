package org.example.orientcompanion.service;

import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.AdminDashboardStatsDTO;
import org.example.orientcompanion.enums.SessionStatus;
import org.example.orientcompanion.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;
    private final FieldRepository fieldRepository;
    private final MentorshipSessionRepository mentorshipSessionRepository;

    @Transactional(readOnly = true)
    public AdminDashboardStatsDTO stats() {
        long totalStudents = userRepository.countStudents();
        long completedAssessments = studentRepository.findAllAssessedStudents().size();
        double completionRate = totalStudents > 0
                ? Math.round(((double) completedAssessments / totalStudents) * 1000.0) / 10.0
                : 0.0;

        AdminDashboardStatsDTO.OverviewStats overview = AdminDashboardStatsDTO.OverviewStats.builder()
                .totalStudents(totalStudents)
                .completedAssessments(completedAssessments)
                .completionRate(completionRate)
                .build();

        long totalSchools = schoolRepository.count();
        long publicSchools = schoolRepository.countAllByType("public");
        long privateSchools = schoolRepository.countAllByType("private");
        long totalFields = fieldRepository.count();

        AdminDashboardStatsDTO.CatalogStats catalog = AdminDashboardStatsDTO.CatalogStats.builder()
                .totalSchools(totalSchools)
                .publicSchools(publicSchools)
                .privateSchools(privateSchools)
                .totalFields(totalFields)
                .build();

        long pendingSessions = mentorshipSessionRepository.countAllByStatus(SessionStatus.REQUESTED);
        long confirmedSessions = mentorshipSessionRepository.countAllByStatus(SessionStatus.SCHEDULED);
        long completedSessions = mentorshipSessionRepository.countAllByStatus(SessionStatus.COMPLETED);
        long activeMentors = userRepository.countCounselors();

        AdminDashboardStatsDTO.MentorshipStats mentorship = AdminDashboardStatsDTO.MentorshipStats.builder()
                .pendingSessions(pendingSessions)
                .confirmedSessions(confirmedSessions)
                .completedSessions(completedSessions)
                .activeMentors(activeMentors)
                .build();

        return AdminDashboardStatsDTO.builder()
                .overview(overview)
                .catalog(catalog)
                .mentorship(mentorship)
                .build();
    }
}