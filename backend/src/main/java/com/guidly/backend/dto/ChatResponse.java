package com.guidly.backend.dto;

import java.math.BigDecimal;

public class ChatResponse {

    private String reply;
    private BigDecimal confidence;
    private ProcedureDTO matchedProcedure; // null si aucune démarche trouvée avec confiance suffisante

    public ChatResponse(String reply, BigDecimal confidence, ProcedureDTO matchedProcedure) {
        this.reply = reply;
        this.confidence = confidence;
        this.matchedProcedure = matchedProcedure;
    }

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }

    public BigDecimal getConfidence() { return confidence; }
    public void setConfidence(BigDecimal confidence) { this.confidence = confidence; }

    public ProcedureDTO getMatchedProcedure() { return matchedProcedure; }
    public void setMatchedProcedure(ProcedureDTO matchedProcedure) { this.matchedProcedure = matchedProcedure; }
}
