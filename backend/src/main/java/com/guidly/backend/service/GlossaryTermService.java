package com.guidly.backend.service;

import com.guidly.backend.dto.GlossaryTermDTO;
import com.guidly.backend.model.GlossaryTerm;
import com.guidly.backend.repository.GlossaryTermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GlossaryTermService {

    @Autowired
    private GlossaryTermRepository glossaryTermRepository;

    public Optional<GlossaryTermDTO> getByTerm(String term) {
        return glossaryTermRepository.findByTermIgnoreCase(term).map(this::toDTO);
    }

    private GlossaryTermDTO toDTO(GlossaryTerm term) {
        GlossaryTermDTO dto = new GlossaryTermDTO();
        dto.setId(term.getId());
        dto.setTerm(term.getTerm());
        dto.setExplanation(term.getExplanation());
        return dto;
    }
}
