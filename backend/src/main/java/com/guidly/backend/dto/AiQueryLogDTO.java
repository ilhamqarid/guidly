package com.guidly.backend.dto;

import java.time.LocalDateTime;

public class AiQueryLogDTO {

    private Long id;
    private String inputText;
    private String detectedLanguage;
    private String detectedIntent;
    private Double confidence;
    private String matchedProcedureTitle; // null si aucune procédure trouvée
    private Integer responseTimeMs;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInputText() { return inputText; }
    public void setInputText(String inputText) { this.inputText = inputText; }

    public String getDetectedLanguage() { return detectedLanguage; }
    public void setDetectedLanguage(String detectedLanguage) { this.detectedLanguage = detectedLanguage; }

    public String getDetectedIntent() { return detectedIntent; }
    public void setDetectedIntent(String detectedIntent) { this.detectedIntent = detectedIntent; }

    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }

    public String getMatchedProcedureTitle() { return matchedProcedureTitle; }
    public void setMatchedProcedureTitle(String matchedProcedureTitle) { this.matchedProcedureTitle = matchedProcedureTitle; }

    public Integer getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(Integer responseTimeMs) { this.responseTimeMs = responseTimeMs; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
