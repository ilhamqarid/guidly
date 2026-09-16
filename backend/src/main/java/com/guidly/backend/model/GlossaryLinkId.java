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
public class GlossaryLinkId implements Serializable {
    private Long procedureId;
    private Long glossaryTermId;

    public GlossaryLinkId(Long procedureId, Long glossaryTermId) {
        this.procedureId = procedureId;
        this.glossaryTermId = glossaryTermId;
    }
}
