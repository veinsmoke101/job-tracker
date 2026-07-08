package com.taha.job_tracker.service;

import com.taha.job_tracker.dto.ApplicationRequest;
import com.taha.job_tracker.dto.ApplicationResponse;
import com.taha.job_tracker.entity.Application;
import com.taha.job_tracker.entity.ApplicationStatus;
import com.taha.job_tracker.entity.Tag;
import com.taha.job_tracker.entity.User;
import com.taha.job_tracker.exception.AccessDeniedException;
import com.taha.job_tracker.exception.ResourceNotFoundException;
import com.taha.job_tracker.mapper.ApplicationMapper;
import com.taha.job_tracker.repository.ApplicationRepository;
import com.taha.job_tracker.repository.TagRepository;
import com.taha.job_tracker.repository.UserRepository;
import com.taha.job_tracker.statemachine.ApplicationStateMachine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ApplicationMapper applicationMapper;


    @Transactional
    public ApplicationResponse create(UUID userId, ApplicationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Set<Tag> tags = resolveTags(request.getTagIds(), userId);

        Application application = Application.builder()
                .user(user)
                .company(request.getCompany())
                .role(request.getRole())
                .status(ApplicationStatus.APPLIED)
                .source(request.getSource())
                .appliedDate(request.getAppliedDate())
                .notes(request.getNotes())
                .tags(tags)
                .build();

        return applicationMapper.toResponse(applicationRepository.save(application));
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getAllByUser(UUID userId) {
        return applicationRepository.findAll().stream()
                .filter(app -> app.getUser().getId().equals(userId))
                .map(applicationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ApplicationResponse getById(UUID userId, UUID applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Access denied");
        }

        return applicationMapper.toResponse(application);
    }

    @Transactional
    public ApplicationResponse update(UUID userId, UUID applicationId, ApplicationRequest request) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Access denied");
        }

        Set<Tag> tags = resolveTags(request.getTagIds(), userId);

        application.setCompany(request.getCompany());
        application.setRole(request.getRole());
        application.setSource(request.getSource());
        application.setAppliedDate(request.getAppliedDate());
        application.setNotes(request.getNotes());
        application.setTags(tags);

        return applicationMapper.toResponse(applicationRepository.save(application));
    }

    @Transactional
    public void delete(UUID userId, UUID applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Access denied");
        }

        applicationRepository.delete(application);
    }

    @Transactional
    public ApplicationResponse updateStatus(UUID userId, UUID applicationId, ApplicationStatus newStatus) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Access denied");
        }

        ApplicationStateMachine.validate(application.getStatus(), newStatus);
        application.setStatus(newStatus);

        return applicationMapper.toResponse(applicationRepository.save(application));
    }

    private Set<Tag> resolveTags(Set<UUID> tagIds, UUID userId) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Set.of();
        }
        return tagIds.stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .filter(tag -> tag.getUser().getId().equals(userId))
                        .orElseThrow(() -> new ResourceNotFoundException("Tag not found or does not belong to user")))
                .collect(Collectors.toSet());
    }

}