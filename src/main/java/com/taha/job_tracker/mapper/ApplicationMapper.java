package com.taha.job_tracker.mapper;

import com.taha.job_tracker.dto.ApplicationResponse;
import com.taha.job_tracker.dto.TagResponse;
import com.taha.job_tracker.entity.Application;
import com.taha.job_tracker.entity.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    ApplicationResponse toResponse(Application application);

    TagResponse toResponse(Tag tag);
}