package com.guidly.backend.dto;

import com.guidly.backend.model.UserProcedureStatus;

import java.time.LocalDateTime;
import java.util.List;

public class UserProcedureDTO {

    private Long id;
    private Long procedureId;
    private String procedureTitle;
    private UserProcedureStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private int completedSteps;
    private int totalSteps;
    private List<StepProgressDTO> steps;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProcedureId() { return procedureId; }
    public void setProcedureId(Long procedureId) { this.procedureId = procedureId; }

    public String getProcedureTitle() { return procedureTitle; }
    public void setProcedureTitle(String procedureTitle) { this.procedureTitle = procedureTitle; }

    public UserProcedureStatus getStatus() { return status; }
    public void setStatus(UserProcedureStatus status) { this.status = status; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public int getCompletedSteps() { return completedSteps; }
    public void setCompletedSteps(int completedSteps) { this.completedSteps = completedSteps; }

    public int getTotalSteps() { return totalSteps; }
    public void setTotalSteps(int totalSteps) { this.totalSteps = totalSteps; }

    public List<StepProgressDTO> getSteps() { return steps; }
    public void setSteps(List<StepProgressDTO> steps) { this.steps = steps; }
}
