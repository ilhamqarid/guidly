package com.guidly.backend.dto;

import com.guidly.backend.model.ProcedureStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/** Utilisé pour la création ET la modification d'une procédure (admin). */
public class ProcedureUpsertRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String intentCode;

    @NotNull
    private Long categoryId;

    @NotNull
    private Long organizationId;

    @NotBlank
    private String description;

    private String targetUsers;
    private String estimatedDuration;
    private String estimatedFees;

    @NotNull
    private ProcedureStatus status;

    @NotNull
    private LocalDate lastVerifiedAt;

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
}
