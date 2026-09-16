package com.guidly.backend.service;

import com.guidly.backend.dto.AiQueryLogDTO;
import com.guidly.backend.model.AiQueryLog;
import com.guidly.backend.repository.AiQueryLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiQueryLogService {

    @Autowired
    private AiQueryLogRepository aiQueryLogRepository;

    /** Les 100 requêtes les plus récentes, du plus récent au plus ancien. */
    public List<AiQueryLogDTO> getRecentLogs() {
        return aiQueryLogRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .limit(100)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private AiQueryLogDTO toDTO(AiQueryLog log) {
        AiQueryLogDTO dto = new AiQueryLogDTO();
        dto.setId(log.getId());
        dto.setInputText(log.getInputText());
        dto.setDetectedLanguage(log.getDetectedLanguage());
        dto.setDetectedIntent(log.getDetectedIntent());
        dto.setConfidence(log.getConfidence() != null ? log.getConfidence().doubleValue() : null);
        dto.setMatchedProcedureTitle(log.getMatchedProcedure() != null ? log.getMatchedProcedure().getTitle() : null);
        dto.setResponseTimeMs(log.getResponseTimeMs());
        dto.setCreatedAt(log.getCreatedAt());
        return dto;
    }
}
