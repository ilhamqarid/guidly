package com.guidly.backend.repository;

import com.guidly.backend.model.Source;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SourceRepository extends JpaRepository<Source, Long> {
    List<Source> findByProcedureId(Long procedureId);
}
