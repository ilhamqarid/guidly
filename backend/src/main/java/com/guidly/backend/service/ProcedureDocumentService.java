package com.guidly.backend.service;

import com.guidly.backend.dto.ProcedureDocumentDTO;
import com.guidly.backend.model.ProcedureDocument;
import com.guidly.backend.repository.ProcedureDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcedureDocumentService {

    @Autowired
    private ProcedureDocumentRepository procedureDocumentRepository;

    public List<ProcedureDocumentDTO> getDocumentsByProcedureId(Long procedureId) {
        return procedureDocumentRepository.findByProcedureId(procedureId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ProcedureDocumentDTO toDTO(ProcedureDocument pd) {
        ProcedureDocumentDTO dto = new ProcedureDocumentDTO();
        dto.setDocumentId(pd.getDocument().getId());
        dto.setName(pd.getDocument().getName());
        dto.setDescription(pd.getDocument().getDescription());
        dto.setType(pd.getDocument().getType());
        dto.setRequired(pd.getRequired());
        dto.setCondition(pd.getCondition());
        return dto;
    }
}
