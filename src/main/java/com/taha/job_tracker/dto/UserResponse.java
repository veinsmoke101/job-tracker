package com.taha.job_tracker.dto;

import com.taha.job_tracker.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserResponse {
    private UUID id;
    private String email;
    private String fullName;
    private Role role;
}