package com.guidly.backend.dto;

import java.util.List;
import java.util.Map;

/**
 * Miroir exact du JSON renvoyé par POST /analyze du service Python.
 * Ce DTO reste interne au backend — le frontend ne voit jamais ça
 * directement, il reçoit AiAnalyzeResponseDTO (enrichi avec la vraie
 * procédure venant de la base de données).
 */
public class AiRawAnalysisDTO {

    private String input;
    private String language;
    private String intent;
    private String intentLabel;
    private Double confidence;
    private String action; // "direct" | "suggest" | "clarify"
    private List<AiIntentAlternativeDTO> alternatives;
    private Map<String, Object> entities;

    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }

    public String getIntentLabel() { return intentLabel; }
    public void setIntentLabel(String intentLabel) { this.intentLabel = intentLabel; }

    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public List<AiIntentAlternativeDTO> getAlternatives() { return alternatives; }
    public void setAlternatives(List<AiIntentAlternativeDTO> alternatives) { this.alternatives = alternatives; }

    public Map<String, Object> getEntities() { return entities; }
    public void setEntities(Map<String, Object> entities) { this.entities = entities; }
}
