package com.guidly.backend.repository;

import com.guidly.backend.model.ProcedureDocument;
import com.guidly.backend.model.ProcedureDocumentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcedureDocumentRepository extends JpaRepository<ProcedureDocument, ProcedureDocumentId> {
    List<ProcedureDocument> findByProcedureId(Long procedureId);
}
