package com.taha.job_tracker.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class TagResponse {
    private UUID id;
    private String name;
}