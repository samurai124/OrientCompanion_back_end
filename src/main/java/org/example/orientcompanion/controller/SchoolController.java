package org.example.orientcompanion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.SchoolRequest;
import org.example.orientcompanion.dto.SchoolResponse;
import org.example.orientcompanion.service.SchoolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Schools", description = "Endpoints de consultation et de gestion des établissements scolaires / universités")
@RestController
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @Operation(summary = "Lister les écoles", description = "Rôles autorisés: STUDENT, COUNSELOR, ADMIN. Permet de filtrer par ID de filière associée.")
    @GetMapping("/api/schools")
    @PreAuthorize("hasAnyRole('STUDENT', 'COUNSELOR', 'ADMIN')")
    public ResponseEntity<List<SchoolResponse>> findAll(
            @RequestParam(required = false) Long fieldId
    ) {
        List<SchoolResponse> schools = fieldId != null
                ? schoolService.findByFieldId(fieldId)
                : schoolService.findAll();

        return ResponseEntity.ok(schools);
    }

    @Operation(summary = "Consulter une école par ID", description = "Rôles autorisés: STUDENT, COUNSELOR, ADMIN.")
    @GetMapping("/api/schools/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'COUNSELOR', 'ADMIN')")
    public ResponseEntity<SchoolResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(schoolService.findById(id));
    }

    @Operation(summary = "Créer une école", description = "Rôle requis: ADMIN.")
    @PostMapping("/api/admin/schools")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SchoolResponse> create(@Valid @RequestBody SchoolRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(schoolService.create(request));
    }

    @Operation(summary = "Modifier une école", description = "Rôle requis: ADMIN.")
    @PutMapping("/api/admin/schools/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SchoolResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SchoolRequest request
    ) {
        return ResponseEntity.ok(schoolService.update(id, request));
    }

    @Operation(summary = "Supprimer une école", description = "Rôle requis: ADMIN.")
    @DeleteMapping("/api/admin/schools/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        schoolService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

