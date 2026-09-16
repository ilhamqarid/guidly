package com.guidly.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "glossary_links")
@Getter
@Setter
@NoArgsConstructor
public class GlossaryLink {

    @EmbeddedId
    private GlossaryLinkId id = new GlossaryLinkId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("procedureId")
    @JoinColumn(name = "procedure_id")
    private Procedure procedure;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("glossaryTermId")
    @JoinColumn(name = "glossary_term_id")
    private GlossaryTerm glossaryTerm;
}
