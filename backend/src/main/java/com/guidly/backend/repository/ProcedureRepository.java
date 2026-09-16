package com.guidly.backend.repository;

import com.guidly.backend.model.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcedureRepository extends JpaRepository<Procedure, Long> {

    // Utilisé par le pipeline IA pour
    // retrouver la procédure correspondant à l'intention détectée.
    Optional<Procedure> findByIntentCode(String intentCode);
}
