package org.example.orientcompanion.service;

import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.CounselorProfileResponse;
import org.example.orientcompanion.dto.MentorshipSessionResponse;
import org.example.orientcompanion.dto.MentorshipSessionUpdateRequest;
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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    @CacheEvict(value = {"mentorship_student", "mentorship_counselor"}, allEntries = true)
    public MentorshipSessionResponse updateStatus(Long sessionId, Long counselorId, MentorshipSessionUpdateRequest request) {
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

        return mentorshipMapper.toResponse(sessionRepository.save(session));
    }
}