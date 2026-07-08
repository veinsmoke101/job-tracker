package com.taha.job_tracker.repository;

import com.taha.job_tracker.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
    List<Tag> findByUserId(UUID userId);
    boolean existsByNameAndUserId(String name, UUID userId);
}