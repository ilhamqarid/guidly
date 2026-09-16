package com.guidly.backend.repository;

import com.guidly.backend.model.ProcedureStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcedureStepRepository extends JpaRepository<ProcedureStep, Long> {
    List<ProcedureStep> findByProcedureIdOrderByStepNumberAsc(Long procedureId);
}
