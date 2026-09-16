package com.guidly.backend.dto;

public class ProcedureStepDTO {

    private Long id;
    private Integer stepNumber;
    private String title;
    private String description;
    private Boolean required;

    public ProcedureStepDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getStepNumber() { return stepNumber; }
    public void setStepNumber(Integer stepNumber) { this.stepNumber = stepNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getRequired() { return required; }
    public void setRequired(Boolean required) { this.required = required; }
}
