package com.guidly.backend.dto;

public class AssistantChatRequestDTO {

    private Long procedureId;
    private String question;

    public AssistantChatRequestDTO() {
    }

    public Long getProcedureId() { return procedureId; }
    public void setProcedureId(Long procedureId) { this.procedureId = procedureId; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
}
