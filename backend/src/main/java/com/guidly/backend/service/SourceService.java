package com.guidly.backend.service;

import com.guidly.backend.dto.SourceDTO;
import com.guidly.backend.model.Source;
import com.guidly.backend.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SourceService {

    @Autowired
    private SourceRepository sourceRepository;

    public List<SourceDTO> getSourcesByProcedureId(Long procedureId) {
        return sourceRepository.findByProcedureId(procedureId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private SourceDTO toDTO(Source source) {
        SourceDTO dto = new SourceDTO();
        dto.setId(source.getId());
        dto.setTitle(source.getTitle());
        dto.setUrl(source.getUrl());
        dto.setLastVerifiedAt(source.getLastVerifiedAt());
        return dto;
    }
}
