package com.guidly.backend.repository;

import com.guidly.backend.model.Procedure;
import com.guidly.backend.model.ProcedureStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProcedureRepository extends JpaRepository<Procedure, Long> {

    // Utilisé par le pipeline IA (Étape C - Procedure Retrieval) pour
    // retrouver la procédure correspondant à l'intention détectée.
    Optional<Procedure> findByIntentCode(String intentCode);

    // Utilisé par le endpoint public : seules les procédures PUBLISHED
    // doivent être visibles des citoyens (jamais DRAFT ni ARCHIVED).
    List<Procedure> findByStatus(ProcedureStatus status);
}
