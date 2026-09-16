package com.guidly.backend.dto;

public class UserDTO {

    private String name;
    private String email;
    private String role;
    private String preferredLanguage;

    public UserDTO(String name, String email, String role, String preferredLanguage) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.preferredLanguage = preferredLanguage;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
}
