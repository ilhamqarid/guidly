package com.guidly.backend.dto;

public class StepProgressDTO {

    private Long stepId;
    private String title;
    private Integer stepNumber;
    private Boolean completed;

    public StepProgressDTO() {}

    public StepProgressDTO(Long stepId, String title, Integer stepNumber, Boolean completed) {
        this.stepId = stepId;
        this.title = title;
        this.stepNumber = stepNumber;
        this.completed = completed;
    }

    public Long getStepId() { return stepId; }
    public void setStepId(Long stepId) { this.stepId = stepId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getStepNumber() { return stepNumber; }
    public void setStepNumber(Integer stepNumber) { this.stepNumber = stepNumber; }

    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
}
