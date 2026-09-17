package org.example.orientcompanion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.AdminAssessmentResponse;
import org.example.orientcompanion.dto.AdminCreateUserRequest;
import org.example.orientcompanion.dto.AdminResetPasswordRequest;
import org.example.orientcompanion.dto.AdminUserResponse;
import org.example.orientcompanion.dto.MentorshipSessionResponse;
import org.example.orientcompanion.dto.UserStatusUpdateRequest;
import org.example.orientcompanion.service.AdminUserService;
import org.example.orientcompanion.service.MentorshipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Admin – Gestion des comptes et assessments",
        description = "Opérations d'administration : utilisateurs, assessments et sessions de mentorat")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final MentorshipService mentorshipService;

    @Operation(summary = "Lister tous les utilisateurs")
    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.findAllUsers());
    }

    @Operation(summary = "Créer un utilisateur (admin, student ou counselor)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Utilisateur créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou email déjà utilisé")
    })
    @PostMapping("/users")
    public ResponseEntity<AdminUserResponse> createUser(
            @Valid @RequestBody AdminCreateUserRequest request
    ) {
        AdminUserResponse created = adminUserService.createUser(
                request.email(), request.password(), request.fullName(), request.role()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Modifier le statut d'un compte (ACTIVE / SUSPENDED)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Statut mis à jour"),
            @ApiResponse(responseCode = "400", description = "Statut invalide"),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    })
    @PatchMapping("/users/{id}/status")
    public ResponseEntity<Void> updateUserStatus(
            @Parameter(description = "Identifiant de l'utilisateur", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UserStatusUpdateRequest request
    ) {
        adminUserService.updateStatus(id, request.status());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Réinitialiser le mot de passe d'un utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Mot de passe réinitialisé"),
            @ApiResponse(responseCode = "400", description = "Mot de passe trop court ou vide"),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    })
    @PatchMapping("/users/{id}/password")
    public ResponseEntity<Void> resetUserPassword(
            @Parameter(description = "Identifiant de l'utilisateur", required = true)
            @PathVariable Long id,
            @Valid @RequestBody AdminResetPasswordRequest request
    ) {
        adminUserService.resetPassword(id, request.newPassword());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Supprimer définitivement un utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Utilisateur supprimé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "Identifiant de l'utilisateur", required = true)
            @PathVariable Long id
    ) {
        adminUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lister tous les assessments étudiants",
            description = "Supporte la pagination via ?page=&size= pour le widget dashboard (recent) ou retourne tout si omis.")
    @GetMapping("/assessments")
    public ResponseEntity<List<AdminAssessmentResponse>> getAssessments(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        List<AdminAssessmentResponse> result = (page != null && size != null)
                ? adminUserService.findRecentAssessments(page, size)
                : adminUserService.findAllAssessments();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Lister toutes les sessions de mentorat (vue admin globale)")
    @GetMapping("/mentorship/sessions")
    public ResponseEntity<List<MentorshipSessionResponse>> getAllMentorshipSessions() {
        return ResponseEntity.ok(mentorshipService.findAllSessions());
    }

    @Operation(summary = "Mettre à jour le statut d'une session de mentorat (vue admin)")
    @PatchMapping("/mentorship/sessions/{id}/status")
    public ResponseEntity<MentorshipSessionResponse> updateSessionStatus(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body
    ) {
        String statusStr = body.get("status");
        if (statusStr == null || statusStr.isBlank()) {
            throw new IllegalArgumentException("Le statut est obligatoire");
        }
        org.example.orientcompanion.enums.SessionStatus status = org.example.orientcompanion.enums.SessionStatus.valueOf(statusStr.toUpperCase());
        return ResponseEntity.ok(mentorshipService.adminUpdateStatus(id, status));
    }

    @Operation(summary = "Récupérer les logs d'audit",
            description = "Retourne un tableau vide en attendant l'intégration d'un système d'audit (ex: Spring Data Envers ou table audit_logs).")
    @GetMapping("/audit-logs")
    public ResponseEntity<List<Object>> getAuditLogs() {

        return ResponseEntity.ok(List.of());
    }
}
