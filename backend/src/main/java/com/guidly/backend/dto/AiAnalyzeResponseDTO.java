package com.guidly.backend.dto;

import java.util.List;
import java.util.Map;

public class AiAnalyzeResponseDTO {

    private String input;
    private String language;
    private String intent;
    private String intentLabel;
    private Double confidence;
    private String action; // "direct" | "suggest" | "clarify"
    private Map<String, Object> entities;

    // Si action == "direct" : la procédure trouvée en base (jamais générée par l'IA)
    private ProcedureDTO matchedProcedure;

    // Si action == "suggest" : 2-3 procédures possibles à proposer à l'utilisateur
    private List<ProcedureDTO> suggestions;

    // Message à afficher côté frontend selon l'action (clarification, hors périmètre...)
    private String message;

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

    public Map<String, Object> getEntities() { return entities; }
    public void setEntities(Map<String, Object> entities) { this.entities = entities; }

    public ProcedureDTO getMatchedProcedure() { return matchedProcedure; }
    public void setMatchedProcedure(ProcedureDTO matchedProcedure) { this.matchedProcedure = matchedProcedure; }

    public List<ProcedureDTO> getSuggestions() { return suggestions; }
    public void setSuggestions(List<ProcedureDTO> suggestions) { this.suggestions = suggestions; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
