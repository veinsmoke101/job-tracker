package com.taha.job_tracker.repository;

import com.taha.job_tracker.entity.Application;
import com.taha.job_tracker.entity.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID>, JpaSpecificationExecutor<Application> {
    long countByUserIdAndStatus(UUID userId, ApplicationStatus status);
}