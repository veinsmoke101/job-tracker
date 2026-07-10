package com.taha.job_tracker.controller;

import com.taha.job_tracker.dto.ApplicationRequest;
import com.taha.job_tracker.dto.ApplicationResponse;
import com.taha.job_tracker.entity.ApplicationStatus;
import com.taha.job_tracker.security.SecurityUtils;
import com.taha.job_tracker.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponse> create(
            @Valid @RequestBody ApplicationRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationService.create(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> getAll() {
        UUID userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(applicationService.getAllByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getById(
            @PathVariable UUID id) {
        UUID userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(applicationService.getById(userId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ApplicationRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(applicationService.update(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id) {
        UUID userId = SecurityUtils.getCurrentUserId();
        applicationService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable UUID id,
            @RequestParam ApplicationStatus status) {
        UUID userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(applicationService.updateStatus(userId, id, status));
    }
}