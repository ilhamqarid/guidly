package com.guidly.backend.dto;

public class AssistantChatResponseDTO {

    private String answer;

    public AssistantChatResponseDTO() {
    }

    public AssistantChatResponseDTO(String answer) {
        this.answer = answer;
    }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}
