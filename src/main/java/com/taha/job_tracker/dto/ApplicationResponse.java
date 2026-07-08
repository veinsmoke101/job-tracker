package com.taha.job_tracker.dto;

import com.taha.job_tracker.entity.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
public class ApplicationResponse {

    private UUID id;
    private String company;
    private String role;
    private ApplicationStatus status;
    private String source;
    private LocalDate appliedDate;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<TagResponse> tags;
}