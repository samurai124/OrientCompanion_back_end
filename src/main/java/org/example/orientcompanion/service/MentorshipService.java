package org.example.orientcompanion.service;

import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.CounselorProfileResponse;
import org.example.orientcompanion.dto.MentorshipSessionResponse;
import org.example.orientcompanion.dto.MentorshipSessionUpdateRequest;
import org.example.orientcompanion.dto.StudentSummaryDTO;
import org.example.orientcompanion.entity.Counselor;
import org.example.orientcompanion.entity.MentorshipSession;
import org.example.orientcompanion.entity.Student;
import org.example.orientcompanion.enums.SessionStatus;
import org.example.orientcompanion.exception.BusinessException;
import org.example.orientcompanion.exception.ResourceNotFoundException;
import org.example.orientcompanion.mapper.CounselorMapper;
import org.example.orientcompanion.mapper.MentorshipMapper;
import org.example.orientcompanion.repository.CounselorRepository;
import org.example.orientcompanion.repository.MentorshipSessionRepository;
import org.example.orientcompanion.repository.StudentRepository;
import org.example.orientcompanion.util.JsonMapCodec;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "mentorship_sessions")
public class MentorshipService {

    private final MentorshipSessionRepository sessionRepository;
    private final CounselorRepository counselorRepository;
    private final StudentRepository studentRepository;
    private final MentorshipMapper mentorshipMapper;
    private final CounselorMapper counselorMapper;

    public List<CounselorProfileResponse> findAllCounselors(Long fieldId) {
        List<Counselor> counselors = fieldId != null
                ? counselorRepository.findBySpecialtyField_Id(fieldId)
                : counselorRepository.findAll();

        return counselors.stream()
                .map(counselorMapper::toResponse)
                .toList();
    }

    public CounselorProfileResponse getCounselorProfile(Long counselorId) {
        Counselor counselor = counselorRepository.findById(counselorId)
                .orElseThrow(() -> new ResourceNotFoundException("Conseiller introuvable"));

        CounselorProfileResponse resp = counselorMapper.toResponse(counselor);

        List<MentorshipSession> sessions = sessionRepository.findByCounselorIdOrderByCreatedAtDesc(counselorId);
        long activeStudents = sessions.stream().map(s -> s.getStudent().getId()).distinct().count();
        long upcoming = sessions.stream().filter(s -> s.getStatus() == SessionStatus.SCHEDULED || s.getStatus() == SessionStatus.REQUESTED).count();
        long completed = sessions.stream().filter(s -> s.getStatus() == SessionStatus.COMPLETED).count();

        resp.setActiveStudentsCount(activeStudents);
        resp.setUpcomingSessionsCount(upcoming);
        resp.setCompletedSessionsCount(completed);
        resp.setAverageRating("4.9 / 5");

        return resp;
    }

    public List<StudentSummaryDTO> findStudentsByCounselor(Long counselorId) {
        List<MentorshipSession> sessions = sessionRepository.findByCounselorIdOrderByCreatedAtDesc(counselorId);
        return sessions.stream()
                .map(MentorshipSession::getStudent)
                .distinct()
                .map(s -> {
                    String dominant = null;
                    if (s.getPersonalityScoresJson() != null) {
                        Map<String, Double> map = JsonMapCodec.fromJson(s.getPersonalityScoresJson());
                        dominant = map.entrySet().stream()
                                .max(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey)
                                .orElse(null);
                    }
                    return StudentSummaryDTO.builder()
                            .id(s.getId())
                            .fullName(s.getFullName())
                            .email(s.getEmail())
                            .dominantRiasec(dominant)
                            .assessmentDate(s.getAssessmentDate())
                            .assessmentDone(s.getAssessmentDate() != null)
                            .build();
                })
                .toList();
    }

    @Transactional
    @CacheEvict(value = {"mentorship_student", "mentorship_counselor"}, allEntries = true)
    public MentorshipSessionResponse requestSession(Long studentId, Long counselorId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Profil étudiant introuvable"));

        Counselor counselor = counselorRepository.findById(counselorId)
                .orElseThrow(() -> new ResourceNotFoundException("Conseiller introuvable"));

        MentorshipSession session = MentorshipSession.builder()
                .student(student)
                .counselor(counselor)
                .status(SessionStatus.REQUESTED)
                .build();

        return mentorshipMapper.toResponse(sessionRepository.save(session));
    }

    @Transactional
    @CacheEvict(value = {"mentorship_student", "mentorship_counselor", "mentorship_sessions"}, allEntries = true)
    public MentorshipSessionResponse adminUpdateStatus(Long sessionId, SessionStatus status) {
        MentorshipSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Séance introuvable"));
        session.setStatus(status);
        return mentorshipMapper.toResponse(sessionRepository.save(session));
    }

    @Cacheable(value = "mentorship_student", key = "#studentId")
    public List<MentorshipSessionResponse> findByStudent(Long studentId) {
        return sessionRepository.findByStudentIdOrderByCreatedAtDesc(studentId)
                .stream()
                .map(mentorshipMapper::toResponse)
                .toList();
    }

    @Cacheable(value = "mentorship_counselor", key = "#counselorId")
    public List<MentorshipSessionResponse> findByCounselor(Long counselorId) {
        return sessionRepository.findByCounselorIdOrderByCreatedAtDesc(counselorId)
                .stream()
                .map(mentorshipMapper::toResponse)
                .toList();
    }

    public List<MentorshipSessionResponse> findAllSessions() {
        return sessionRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(mentorshipMapper::toResponse)
                .toList();
    }

    @Transactional
    @CacheEvict(value = {"mentorship_student", "mentorship_counselor"}, allEntries = true)
    public MentorshipSessionResponse updateStatus(Long sessionId, Long counselorId, MentorshipSessionUpdateRequest request) {
        MentorshipSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Séance introuvable"));

        if (!session.getCounselor().getId().equals(counselorId)) {
            throw new BusinessException("Cette séance ne vous est pas assignée");
        }

        if (request.getStatus() != null) {
            if (request.getStatus() == SessionStatus.SCHEDULED && request.getScheduledAt() == null && session.getScheduledAt() == null) {
                throw new BusinessException("La date de la séance est obligatoire pour la planifier");
            }
            session.setStatus(request.getStatus());
        } else if (request.getScheduledAt() != null) {
            session.setStatus(SessionStatus.SCHEDULED);
        }

        if (request.getScheduledAt() != null) {
            session.setScheduledAt(request.getScheduledAt());
        }

        if (request.getMeetLink() != null) {
            session.setMeetLink(request.getMeetLink());
        }

        return mentorshipMapper.toResponse(sessionRepository.save(session));
    }
}