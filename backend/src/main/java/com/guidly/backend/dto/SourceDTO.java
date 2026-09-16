package com.guidly.backend.dto;

import java.time.LocalDate;

public class SourceDTO {

    private Long id;
    private String title;
    private String url;
    private LocalDate lastVerifiedAt;

    public SourceDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public LocalDate getLastVerifiedAt() { return lastVerifiedAt; }
    public void setLastVerifiedAt(LocalDate lastVerifiedAt) { this.lastVerifiedAt = lastVerifiedAt; }
}
