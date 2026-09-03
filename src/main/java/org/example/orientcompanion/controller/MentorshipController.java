package org.example.orientcompanion.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.repository.StudentRepository;
import org.example.orientcompanion.exception.ResourceNotFoundException;
import org.example.orientcompanion.dto.MentorshipSessionRequest;
import org.example.orientcompanion.dto.MentorshipSessionResponse;
import org.example.orientcompanion.dto.MentorshipSessionUpdateRequest;
import org.example.orientcompanion.entity.MentorshipSession;
import org.example.orientcompanion.mapper.MentorshipMapper;
import org.example.orientcompanion.service.MentorshipService;
import org.example.orientcompanion.entity.Student;
import org.example.orientcompanion.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MentorshipController {

    private final MentorshipService mentorshipService;
    private final StudentRepository studentRepository;
    private final MentorshipMapper mentorshipMapper;

    // --- Côté étudiant ---

    @PostMapping("/api/student/mentorship/sessions")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<MentorshipSessionResponse> requestSession(
            @AuthenticationPrincipal User principal,
            @Valid @RequestBody MentorshipSessionRequest request
    ) {
        Student student = studentRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profil étudiant introuvable"));

        MentorshipSession session = mentorshipService.requestSession(student, request.getCounselorId());

        return ResponseEntity.status(HttpStatus.CREATED).body(mentorshipMapper.toResponse(session));
    }

    @GetMapping("/api/student/mentorship/sessions")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<MentorshipSessionResponse>> getMySessionsAsStudent(
            @AuthenticationPrincipal User principal
    ) {
        List<MentorshipSession> sessions = mentorshipService.findByStudent(principal.getId());
        return ResponseEntity.ok(sessions.stream().map(mentorshipMapper::toResponse).toList());
    }

    // --- Côté conseiller ---

    @GetMapping("/api/counselor/mentorship/sessions")
    @PreAuthorize("hasRole('COUNSELOR')")
    public ResponseEntity<List<MentorshipSessionResponse>> getMySessionsAsCounselor(
            @AuthenticationPrincipal User principal
    ) {
        List<MentorshipSession> sessions = mentorshipService.findByCounselor(principal.getId());
        return ResponseEntity.ok(sessions.stream().map(mentorshipMapper::toResponse).toList());
    }

    @PatchMapping("/api/counselor/mentorship/sessions/{id}")
    @PreAuthorize("hasRole('COUNSELOR')")
    public ResponseEntity<MentorshipSessionResponse> updateSession(
            @AuthenticationPrincipal User principal,
            @PathVariable Long id,
            @Valid @RequestBody MentorshipSessionUpdateRequest request
    ) {
        MentorshipSession session = mentorshipService.updateStatus(id, principal.getId(), request);
        return ResponseEntity.ok(mentorshipMapper.toResponse(session));
    }
}