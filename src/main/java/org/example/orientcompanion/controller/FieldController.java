package org.example.orientcompanion.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.FieldRequest;
import org.example.orientcompanion.dto.FieldResponse;
import org.example.orientcompanion.entity.Field;
import org.example.orientcompanion.mapper.FieldMapper;
import org.example.orientcompanion.service.FieldService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FieldController {

    private final FieldService fieldService;
    private final FieldMapper fieldMapper;


    @GetMapping("/api/fields")
    public ResponseEntity<List<FieldResponse>> findAll(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search
    ) {
        List<Field> fields;

        if (search != null && !search.isBlank()) {
            fields = fieldService.search(search);
        } else if (category != null && !category.isBlank()) {
            fields = fieldService.findByCategory(category);
        } else {
            fields = fieldService.findAll();
        }

        return ResponseEntity.ok(fields.stream().map(fieldMapper::toResponse).toList());
    }

    @GetMapping("/api/fields/{id}")
    public ResponseEntity<FieldResponse> findById(@PathVariable Long id) {
        Field field = fieldService.findById(id);
        return ResponseEntity.ok(fieldMapper.toResponse(field));
    }


    @PostMapping("/api/admin/fields")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FieldResponse> create(@Valid @RequestBody FieldRequest request) {
        Field field = fieldService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(fieldMapper.toResponse(field));
    }

    @PutMapping("/api/admin/fields/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FieldResponse> update(@PathVariable Long id, @Valid @RequestBody FieldRequest request) {
        Field field = fieldService.update(id, request);
        return ResponseEntity.ok(fieldMapper.toResponse(field));
    }

    @DeleteMapping("/api/admin/fields/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fieldService.delete(id);
        return ResponseEntity.noContent().build();
    }
}