package org.example.orientcompanion.controller;

import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.AdminDashboardStatsDTO;
import org.example.orientcompanion.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatsController {

    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<AdminDashboardStatsDTO> getAdminDashboardStats() {
        return ResponseEntity.ok(statsService.stats());
    }
}