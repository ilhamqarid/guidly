package com.guidly.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "procedure_documents")
@Getter
@Setter
@NoArgsConstructor
public class ProcedureDocument {

    @EmbeddedId
    private ProcedureDocumentId id = new ProcedureDocumentId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("procedureId")
    @JoinColumn(name = "procedure_id")
    private Procedure procedure;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("documentId")
    @JoinColumn(name = "document_id")
    private Document document;

    @Column(nullable = false)
    private Boolean required = true;

    @Column(length = 255)
    private String condition; // ex: "si première demande"
}
