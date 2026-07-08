package com.taha.job_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ApplicationRequest {

    @NotBlank(message = "Company is required")
    private String company;

    @NotBlank(message = "Role is required")
    private String role;

    @NotNull(message = "Applied date is required")
    private LocalDate appliedDate;

    private String source;
    private String notes;
    private Set<UUID> tagIds;
}