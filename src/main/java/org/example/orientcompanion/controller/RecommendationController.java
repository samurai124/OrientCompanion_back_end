package org.example.orientcompanion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.RecommendationResponse;
import org.example.orientcompanion.entity.User;
import org.example.orientcompanion.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Recommendations", description = "Endpoints des recommandations d'orientation pour étudiants (ROLE_STUDENT)")
@RestController
@RequestMapping("/api/student/recommendations")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(summary = "Obtenir mes recommandations", description = "Rôles autorisés: STUDENT, ADMIN. Récupère la liste des filières recommandées classées par score avec explications et écoles associées.")
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<List<RecommendationResponse>> getMyRecommendations(
            @AuthenticationPrincipal User principal
    ) {
        return ResponseEntity.ok(recommendationService.getRecommendations(principal.getId()));
    }

    @Operation(summary = "Régénérer les recommandations", description = "Rôles autorisés: STUDENT, ADMIN. Relance le calcul des scores et explications basé sur le profil actuel de l'étudiant.")
    @PostMapping("/regenerate")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<List<RecommendationResponse>> regenerate(@AuthenticationPrincipal User principal) {
        return ResponseEntity.ok(recommendationService.generateRecommendations(principal.getId()));
    }
}