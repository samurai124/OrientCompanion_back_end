package org.example.orientcompanion.service;

import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.exception.BusinessException;
import org.example.orientcompanion.exception.ResourceNotFoundException;
import org.example.orientcompanion.dto.MentorshipSessionUpdateRequest;
import org.example.orientcompanion.entity.MentorshipSession;
import org.example.orientcompanion.enums.SessionStatus;
import org.example.orientcompanion.repository.CounselorRepository;
import org.example.orientcompanion.repository.MentorshipSessionRepository;
import org.example.orientcompanion.entity.Counselor;
import org.example.orientcompanion.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorshipService {

    private final MentorshipSessionRepository sessionRepository;
    private final CounselorRepository counselorRepository;

    public MentorshipSession requestSession(Student student, Long counselorId) {
        Counselor counselor = counselorRepository.findById(counselorId)
                .orElseThrow(() -> new ResourceNotFoundException("Conseiller introuvable"));

        MentorshipSession session = MentorshipSession.builder()
                .student(student)
                .counselor(counselor)
                .status(SessionStatus.REQUESTED)
                .build();

        return sessionRepository.save(session);
    }

    public List<MentorshipSession> findByStudent(Long studentId) {
        return sessionRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
    }

    public List<MentorshipSession> findByCounselor(Long counselorId) {
        return sessionRepository.findByCounselorIdOrderByCreatedAtDesc(counselorId);
    }

    /**
     * Seul le conseiller assigné à la séance peut la faire évoluer
     * (REQUESTED -> SCHEDULED -> COMPLETED).
     */
    public MentorshipSession updateStatus(Long sessionId, Long counselorId, MentorshipSessionUpdateRequest request) {
        MentorshipSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Séance introuvable"));

        if (!session.getCounselor().getId().equals(counselorId)) {
            throw new BusinessException("Cette séance ne vous est pas assignée");
        }

        if (request.getStatus() == SessionStatus.SCHEDULED && request.getScheduledAt() == null) {
            throw new BusinessException("La date de la séance est obligatoire pour la planifier");
        }

        session.setStatus(request.getStatus());
        if (request.getScheduledAt() != null) {
            session.setScheduledAt(request.getScheduledAt());
        }

        return sessionRepository.save(session);
    }
}