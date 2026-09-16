package com.guidly.backend.dto;

import jakarta.validation.constraints.NotNull;

public class StartProcedureRequest {

    @NotNull
    private Long procedureId;

    public Long getProcedureId() { return procedureId; }
    public void setProcedureId(Long procedureId) { this.procedureId = procedureId; }
}
