package com.guidly.backend.repository;

import com.guidly.backend.model.GlossaryTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GlossaryTermRepository extends JpaRepository<GlossaryTerm, Long> {
    Optional<GlossaryTerm> findByTermIgnoreCase(String term);
}
