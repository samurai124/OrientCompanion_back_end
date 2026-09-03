package org.example.orientcompanion.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.SchoolRequest;
import org.example.orientcompanion.dto.SchoolResponse;
import org.example.orientcompanion.entity.School;
import org.example.orientcompanion.service.SchoolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public read endpoints (/api/schools) are accessible by all authenticated users.
 * Write endpoints (/api/admin/schools) are restricted to ADMIN role.
 */
@RestController
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    // ----------------------------------------------------------------
    // Public / student-facing read endpoints
    // ----------------------------------------------------------------

    @GetMapping("/api/schools")
    public ResponseEntity<List<SchoolResponse>> findAll(
            @RequestParam(required = false) Long fieldId
    ) {
        List<School> schools = fieldId != null
                ? schoolService.findByFieldId(fieldId)
                : schoolService.findAll();

        return ResponseEntity.ok(schools.stream().map(this::toResponse).toList());
    }

    @GetMapping("/api/schools/{id}")
    public ResponseEntity<SchoolResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(schoolService.findById(id)));
    }

    // ----------------------------------------------------------------
    // Admin write endpoints
    // ----------------------------------------------------------------

    @PostMapping("/api/admin/schools")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SchoolResponse> create(@Valid @RequestBody SchoolRequest request) {
        School school = schoolService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(school));
    }

    @PutMapping("/api/admin/schools/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SchoolResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SchoolRequest request
    ) {
        School school = schoolService.update(id, request);
        return ResponseEntity.ok(toResponse(school));
    }

    @DeleteMapping("/api/admin/schools/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        schoolService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ----------------------------------------------------------------
    // Inline mapper (no MapStruct needed for this simple conversion)
    // ----------------------------------------------------------------

    private SchoolResponse toResponse(School school) {
        return SchoolResponse.builder()
                .id(school.getId())
                .name(school.getName())
                .city(school.getCity())
                .country(school.getCountry())
                .type(school.getType())
                .website(school.getWebsite())
                .description(school.getDescription())
                .build();
    }
}
