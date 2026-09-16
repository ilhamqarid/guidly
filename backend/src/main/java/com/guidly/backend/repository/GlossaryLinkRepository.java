package com.guidly.backend.repository;

import com.guidly.backend.model.GlossaryLink;
import com.guidly.backend.model.GlossaryLinkId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GlossaryLinkRepository extends JpaRepository<GlossaryLink, GlossaryLinkId> {
    List<GlossaryLink> findByProcedureId(Long procedureId);
}
