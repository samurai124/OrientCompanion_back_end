package org.example.orientcompanion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.CounselorProfileResponse;
import org.example.orientcompanion.dto.MentorshipSessionRequest;
import org.example.orientcompanion.dto.MentorshipSessionResponse;
import org.example.orientcompanion.dto.MentorshipSessionUpdateRequest;
import org.example.orientcompanion.entity.User;
import org.example.orientcompanion.service.MentorshipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Mentorship", description = "Endpoints de gestion du mentorat (Étudiants et Conseillers)")
@RestController
@RequiredArgsConstructor
public class MentorshipController {

    private final MentorshipService mentorshipService;

    @Operation(summary = "Lister les conseillers disponibles", description = "Rôle requis: STUDENT. Permet de filtrer par ID de filière.")
    @GetMapping("/api/student/mentorship/counselors")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<List<CounselorProfileResponse>> getAvailableCounselors(
            @RequestParam(required = false) Long fieldId
    ) {
        return ResponseEntity.ok(mentorshipService.findAllCounselors(fieldId));
    }

    @Operation(summary = "Demander une séance de mentorat", description = "Rôle requis: STUDENT. Crée une demande de séance avec un conseiller.")
    @PostMapping("/api/student/mentorship/sessions")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<MentorshipSessionResponse> requestSession(
            @AuthenticationPrincipal User principal,
            @Valid @RequestBody MentorshipSessionRequest request
    ) {
        MentorshipSessionResponse response = mentorshipService.requestSession(principal.getId(), request.getCounselorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Consulter mes séances en tant qu'étudiant", description = "Rôle requis: STUDENT. Récupère l'historique des séances demandées.")
    @GetMapping("/api/student/mentorship/sessions")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<List<MentorshipSessionResponse>> getMySessionsAsStudent(
            @AuthenticationPrincipal User principal
    ) {
        return ResponseEntity.ok(mentorshipService.findByStudent(principal.getId()));
    }

    @Operation(summary = "Consulter mes séances en tant que conseiller", description = "Rôle requis: COUNSELOR. Récupère les séances assignées.")
    @GetMapping("/api/counselor/mentorship/sessions")
    @PreAuthorize("hasAnyRole('COUNSELOR','ADMIN')")
    public ResponseEntity<List<MentorshipSessionResponse>> getMySessionsAsCounselor(
            @AuthenticationPrincipal User principal
    ) {
        return ResponseEntity.ok(mentorshipService.findByCounselor(principal.getId()));
    }

    @Operation(summary = "Mettre à jour le statut ou planifier une séance", description = "Rôle requis: COUNSELOR. Permet d'accepter, refuser ou planifier une séance.")
    @PatchMapping("/api/counselor/mentorship/sessions/{id}")
    @PreAuthorize("hasAnyRole('COUNSELOR','ADMIN')")
    public ResponseEntity<MentorshipSessionResponse> updateSession(
            @AuthenticationPrincipal User principal,
            @PathVariable Long id,
            @Valid @RequestBody MentorshipSessionUpdateRequest request
    ) {
        return ResponseEntity.ok(mentorshipService.updateStatus(id, principal.getId(), request));
    }
}