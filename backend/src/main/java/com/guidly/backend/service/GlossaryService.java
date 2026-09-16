package com.guidly.backend.service;

import com.guidly.backend.dto.GlossaryTermDTO;
import com.guidly.backend.model.GlossaryLink;
import com.guidly.backend.model.GlossaryTerm;
import com.guidly.backend.repository.GlossaryLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Section 9 du cahier des charges : "L'utilisateur doit pouvoir cliquer
 * sur un terme difficile." Ce service ne fait QUE relire des explications
 * déjà écrites en base (glossary_terms) — il ne génère jamais une
 * définition à la volée avec une IA, pour rester fidèle au principe
 * "ne jamais inventer" (section 4).
 */
@Service
public class GlossaryService {

    @Autowired
    private GlossaryLinkRepository glossaryLinkRepository;

    @Transactional(readOnly = true)
    public List<GlossaryTermDTO> getTermsForProcedure(Long procedureId) {
        List<GlossaryLink> links = glossaryLinkRepository.findByProcedureId(procedureId);
        return links.stream()
                .map(link -> toDTO(link.getGlossaryTerm()))
                .collect(Collectors.toList());
    }

    private GlossaryTermDTO toDTO(GlossaryTerm term) {
        GlossaryTermDTO dto = new GlossaryTermDTO();
        dto.setId(term.getId());
        dto.setTerm(term.getTerm());
        dto.setExplanation(term.getExplanation());
        if (term.getSource() != null) {
            dto.setSourceTitle(term.getSource().getTitle());
            dto.setSourceUrl(term.getSource().getUrl());
        }
        return dto;
    }
}
