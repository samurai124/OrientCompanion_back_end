package org.example.orientcompanion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.FieldRequest;
import org.example.orientcompanion.dto.FieldResponse;
import org.example.orientcompanion.service.FieldService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Fields", description = "Endpoints de consultation et de gestion des filières d'orientation")
@RestController
@RequiredArgsConstructor
public class FieldController {

    private final FieldService fieldService;

    @Operation(summary = "Lister les filières", description = "Rôles autorisés: STUDENT, COUNSELOR, ADMIN. Permet de filtrer par catégorie ou recherche textuelle.")
    @GetMapping("/api/fields")
    @PreAuthorize("hasAnyRole('STUDENT', 'COUNSELOR', 'ADMIN')")
    public ResponseEntity<List<FieldResponse>> findAll(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search
    ) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(fieldService.search(search));
        } else if (category != null && !category.isBlank()) {
            return ResponseEntity.ok(fieldService.findByCategory(category));
        } else {
            return ResponseEntity.ok(fieldService.findAll());
        }
    }

    @Operation(summary = "Consulter une filière par ID", description = "Rôles autorisés: STUDENT, COUNSELOR, ADMIN.")
    @GetMapping("/api/fields/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'COUNSELOR', 'ADMIN')")
    public ResponseEntity<FieldResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(fieldService.findById(id));
    }

    @Operation(summary = "Créer une filière", description = "Rôle requis: ADMIN.")
    @PostMapping("/api/admin/fields")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FieldResponse> create(@Valid @RequestBody FieldRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fieldService.create(request));
    }

    @Operation(summary = "Modifier une filière", description = "Rôle requis: ADMIN.")
    @PutMapping("/api/admin/fields/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FieldResponse> update(@PathVariable Long id, @Valid @RequestBody FieldRequest request) {
        return ResponseEntity.ok(fieldService.update(id, request));
    }

    @Operation(summary = "Supprimer une filière", description = "Rôle requis: ADMIN.")
    @DeleteMapping("/api/admin/fields/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fieldService.delete(id);
        return ResponseEntity.noContent().build();
    }
}