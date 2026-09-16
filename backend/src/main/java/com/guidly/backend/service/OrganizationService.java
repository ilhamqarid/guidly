package com.guidly.backend.service;

import com.guidly.backend.dto.OrganizationDTO;
import com.guidly.backend.model.Organization;
import com.guidly.backend.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public List<OrganizationDTO> getAllOrganizations() {
        return organizationRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private OrganizationDTO toDTO(Organization organization) {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setId(organization.getId());
        dto.setName(organization.getName());
        dto.setDescription(organization.getDescription());
        dto.setAddress(organization.getAddress());
        dto.setCity(organization.getCity());
        dto.setWebsite(organization.getWebsite());
        return dto;
    }
}
