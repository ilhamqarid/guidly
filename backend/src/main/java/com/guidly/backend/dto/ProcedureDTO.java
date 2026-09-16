package com.guidly.backend.dto;

import com.guidly.backend.model.ProcedureStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProcedureDTO {

    private Long id;
    private String title;
    private String intentCode;
    private Long categoryId;
    private Long organizationId;
    private String description;
    private String targetUsers;
    private String estimatedDuration;
    private String estimatedFees;
    private ProcedureStatus status;
    private LocalDate lastVerifiedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProcedureDTO() {
    }

    // --- Getters et setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIntentCode() { return intentCode; }
    public void setIntentCode(String intentCode) { this.intentCode = intentCode; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTargetUsers() { return targetUsers; }
    public void setTargetUsers(String targetUsers) { this.targetUsers = targetUsers; }

    public String getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(String estimatedDuration) { this.estimatedDuration = estimatedDuration; }

    public String getEstimatedFees() { return estimatedFees; }
    public void setEstimatedFees(String estimatedFees) { this.estimatedFees = estimatedFees; }

    public ProcedureStatus getStatus() { return status; }
    public void setStatus(ProcedureStatus status) { this.status = status; }

    public LocalDate getLastVerifiedAt() { return lastVerifiedAt; }
    public void setLastVerifiedAt(LocalDate lastVerifiedAt) { this.lastVerifiedAt = lastVerifiedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
