package com.taha.job_tracker.statemachine;

import com.taha.job_tracker.entity.ApplicationStatus;
import com.taha.job_tracker.exception.BusinessRuleException;

import java.util.Map;
import java.util.Set;

public class ApplicationStateMachine {

    private static final Map<ApplicationStatus, Set<ApplicationStatus>> ALLOWED_TRANSITIONS = Map.of(
            ApplicationStatus.APPLIED, Set.of(ApplicationStatus.INTERVIEWING, ApplicationStatus.REJECTED),
            ApplicationStatus.INTERVIEWING, Set.of(ApplicationStatus.OFFER, ApplicationStatus.REJECTED),
            ApplicationStatus.OFFER, Set.of(ApplicationStatus.ACCEPTED, ApplicationStatus.REJECTED),
            ApplicationStatus.ACCEPTED, Set.of(),
            ApplicationStatus.REJECTED, Set.of()
    );

    public static void validate(ApplicationStatus current, ApplicationStatus next) {
        Set<ApplicationStatus> allowed = ALLOWED_TRANSITIONS.get(current);
        if (!allowed.contains(next)) {
            throw new BusinessRuleException(
                    String.format("Cannot transition from %s to %s", current, next)
            );
        }
    }
}