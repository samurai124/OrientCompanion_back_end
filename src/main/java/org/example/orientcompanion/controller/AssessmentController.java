package org.example.orientcompanion.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.AssessmentRequest;
import org.example.orientcompanion.dto.StudentProfileResponse;
import org.example.orientcompanion.service.AssessmentService;
import org.example.orientcompanion.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student/assessment")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class AssessmentController {

    private final AssessmentService assessmentService;

    @PostMapping
    public ResponseEntity<StudentProfileResponse> submit(
            @AuthenticationPrincipal User principal,
            @Valid @RequestBody AssessmentRequest request
    ) {
        StudentProfileResponse response = assessmentService.submitAssessment(principal.getId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<StudentProfileResponse> getProfile(@AuthenticationPrincipal User principal) {
        StudentProfileResponse response = assessmentService.getProfile(principal.getId());
        return ResponseEntity.ok(response);
    }
}