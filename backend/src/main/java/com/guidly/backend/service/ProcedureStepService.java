package com.guidly.backend.service;

import com.guidly.backend.dto.ProcedureStepDTO;
import com.guidly.backend.model.ProcedureStep;
import com.guidly.backend.repository.ProcedureStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcedureStepService {

    @Autowired
    private ProcedureStepRepository procedureStepRepository;

    public List<ProcedureStepDTO> getStepsByProcedureId(Long procedureId) {
        return procedureStepRepository.findByProcedureIdOrderByStepNumberAsc(procedureId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ProcedureStepDTO toDTO(ProcedureStep step) {
        ProcedureStepDTO dto = new ProcedureStepDTO();
        dto.setId(step.getId());
        dto.setStepNumber(step.getStepNumber());
        dto.setTitle(step.getTitle());
        dto.setDescription(step.getDescription());
        dto.setRequired(step.getRequired());
        return dto;
    }
}
