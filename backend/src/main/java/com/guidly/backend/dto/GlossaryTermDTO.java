package com.guidly.backend.dto;

public class GlossaryTermDTO {

    private Long id;
    private String term;
    private String explanation;

    // Optionnels : présents seulement si le terme a une source rattachée
    private String sourceTitle;
    private String sourceUrl;

    public GlossaryTermDTO() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public String getSourceTitle() { return sourceTitle; }
    public void setSourceTitle(String sourceTitle) { this.sourceTitle = sourceTitle; }

    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
}
