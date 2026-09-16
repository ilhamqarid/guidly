package com.guidly.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "procedures")
public class Procedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "intent_code", nullable = false, unique = true, length = 100)
    private String intentCode;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "target_users", length = 255)
    private String targetUsers;

    @Column(name = "estimated_duration", length = 100)
    private String estimatedDuration;

    @Column(name = "estimated_fees", length = 100)
    private String estimatedFees;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcedureStatus status;

    @Column(name = "last_verified_at", nullable = false)
    private LocalDate lastVerifiedAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

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
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}