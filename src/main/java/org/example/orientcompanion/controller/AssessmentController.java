package org.example.orientcompanion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.AssessmentRequest;
import org.example.orientcompanion.dto.StudentProfileResponse;
import org.example.orientcompanion.entity.User;
import org.example.orientcompanion.service.AssessmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Assessment", description = "Endpoints du bilan d'orientation pour étudiants (ROLE_STUDENT)")
@RestController
@RequestMapping("/api/student/assessment")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
public class AssessmentController {

    private final AssessmentService assessmentService;

    @Operation(summary = "Soumettre le bilan d'orientation", description = "Rôle requis: STUDENT. Enregistre les intérêts, scores RIASEC et notes académiques, calcule le vecteur de profil et génère les recommandations initiales.")
    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<StudentProfileResponse> submit(
            @AuthenticationPrincipal User principal,
            @Valid @RequestBody AssessmentRequest request
    ) {
        StudentProfileResponse response = assessmentService.submitAssessment(principal.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consulter le profil du bilan", description = "Rôle requis: STUDENT. Récupère le profil d'évaluation de l'étudiant connecté.")
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<StudentProfileResponse> getProfile(@AuthenticationPrincipal User principal) {
        StudentProfileResponse response = assessmentService.getProfile(principal.getId());
        return ResponseEntity.ok(response);
    }
}