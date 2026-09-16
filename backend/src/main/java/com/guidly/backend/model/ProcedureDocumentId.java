package com.guidly.backend.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ProcedureDocumentId implements Serializable {
    private Long procedureId;
    private Long documentId;

    public ProcedureDocumentId(Long procedureId, Long documentId) {
        this.procedureId = procedureId;
        this.documentId = documentId;
    }
}
