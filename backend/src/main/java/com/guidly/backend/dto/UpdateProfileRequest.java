package com.guidly.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateProfileRequest {

    @NotBlank
    private String name;

    private String preferredLanguage; // "fr", "en", "ar", "dar"

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
}
