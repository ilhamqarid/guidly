package com.guidly.backend.dto;

public class AiIntentAlternativeDTO {

    private String intent;
    private String label;
    private Double confidence;

    public AiIntentAlternativeDTO() {
    }

    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }
}
