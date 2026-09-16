package com.guidly.backend.service;

import com.guidly.backend.dto.ProcedureDTO;
import com.guidly.backend.dto.ProcedureUpsertRequest;
import com.guidly.backend.model.Category;
import com.guidly.backend.model.Organization;
import com.guidly.backend.model.Procedure;
import com.guidly.backend.model.ProcedureStatus;
import com.guidly.backend.repository.CategoryRepository;
import com.guidly.backend.repository.OrganizationRepository;
import com.guidly.backend.repository.ProcedureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProcedureService {

    @Autowired
    private ProcedureRepository procedureRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    public List<ProcedureDTO> getAllProcedures() {
        return procedureRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProcedureDTO> getProcedureById(Long id) {
        return procedureRepository.findById(id)
                .map(this::toDTO);
    }

    /** Création d'une procédure (admin uniquement). */
    public ProcedureDTO createProcedure(ProcedureUpsertRequest request) {
        Procedure procedure = new Procedure();
        applyRequestToEntity(request, procedure);
        procedureRepository.save(procedure);
        return toDTO(procedure);
    }

    /** Modification d'une procédure existante (admin uniquement). */
    public Optional<ProcedureDTO> updateProcedure(Long id, ProcedureUpsertRequest request) {
        return procedureRepository.findById(id).map(procedure -> {
            applyRequestToEntity(request, procedure);
            procedureRepository.save(procedure);
            return toDTO(procedure);
        });
    }

    /**
     * "Suppression" d'une procédure = archivage, jamais une suppression physique
     * (règle de gestion : on ne casse pas l'historique des utilisateurs qui
     * l'ont déjà suivie — voir docs/02-modele-donnees.md).
     */
    public boolean archiveProcedure(Long id) {
        return procedureRepository.findById(id).map(procedure -> {
            procedure.setStatus(ProcedureStatus.ARCHIVED);
            procedureRepository.save(procedure);
            return true;
        }).orElse(false);
    }

    private void applyRequestToEntity(ProcedureUpsertRequest request, Procedure procedure) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Catégorie introuvable : " + request.getCategoryId()));
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new IllegalArgumentException("Organisme introuvable : " + request.getOrganizationId()));

        procedure.setTitle(request.getTitle());
        procedure.setIntentCode(request.getIntentCode());
        procedure.setCategoryId(category.getId());
        procedure.setOrganizationId(organization.getId());
        procedure.setDescription(request.getDescription());
        procedure.setTargetUsers(request.getTargetUsers());
        procedure.setEstimatedDuration(request.getEstimatedDuration());
        procedure.setEstimatedFees(request.getEstimatedFees());
        procedure.setStatus(request.getStatus());
        procedure.setLastVerifiedAt(request.getLastVerifiedAt());
    }

    private ProcedureDTO toDTO(Procedure procedure) {
        ProcedureDTO dto = new ProcedureDTO();
        dto.setId(procedure.getId());
        dto.setTitle(procedure.getTitle());
        dto.setIntentCode(procedure.getIntentCode());
        dto.setCategoryId(procedure.getCategoryId());
        dto.setOrganizationId(procedure.getOrganizationId());
        dto.setDescription(procedure.getDescription());
        dto.setTargetUsers(procedure.getTargetUsers());
        dto.setEstimatedDuration(procedure.getEstimatedDuration());
        dto.setEstimatedFees(procedure.getEstimatedFees());
        dto.setStatus(procedure.getStatus());
        dto.setLastVerifiedAt(procedure.getLastVerifiedAt());
        dto.setCreatedAt(procedure.getCreatedAt());
        dto.setUpdatedAt(procedure.getUpdatedAt());
        return dto;
    }
}
