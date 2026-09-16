package com.guidly.backend.repository;

import com.guidly.backend.model.UserStepProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStepProgressRepository extends JpaRepository<UserStepProgress, Long> {
    List<UserStepProgress> findByUserProcedureId(Long userProcedureId);
    Optional<UserStepProgress> findByUserProcedureIdAndStepId(Long userProcedureId, Long stepId);
}
