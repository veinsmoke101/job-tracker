package com.taha.job_tracker.repository;

import com.taha.job_tracker.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, UUID> {
    List<Interview> findByApplicationIdIn(List<UUID> applicationIds);
    List<Interview> findByScheduledAtBetween(LocalDateTime from, LocalDateTime to);
}