package com.guidly.backend.dto;

public class ProcedureDocumentDTO {

    private Long documentId;
    private String name;
    private String description;
    private String type;
    private Boolean required;
    private String condition;

    public ProcedureDocumentDTO() {}

    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Boolean getRequired() { return required; }
    public void setRequired(Boolean required) { this.required = required; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
}
