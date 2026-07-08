package com.taha.job_tracker.repository;

import com.taha.job_tracker.entity.ResumeVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResumeVersionRepository extends JpaRepository<ResumeVersion, UUID> {
    List<ResumeVersion> findByApplicationIdOrderByVersionNumberDesc(UUID applicationId);
}