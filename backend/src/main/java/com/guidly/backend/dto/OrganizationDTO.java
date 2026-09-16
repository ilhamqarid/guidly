package com.guidly.backend.dto;

public class OrganizationDTO {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String website;

    public OrganizationDTO() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
}
