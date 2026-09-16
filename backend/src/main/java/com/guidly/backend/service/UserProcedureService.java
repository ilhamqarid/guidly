package com.guidly.backend.service;

import com.guidly.backend.dto.StepProgressDTO;
import com.guidly.backend.dto.UserProcedureDTO;
import com.guidly.backend.model.*;
import com.guidly.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProcedureService {

    @Autowired private UserProcedureRepository userProcedureRepository;
    @Autowired private UserStepProgressRepository userStepProgressRepository;
    @Autowired private ProcedureRepository procedureRepository;
    @Autowired private ProcedureStepRepository procedureStepRepository;
    @Autowired private UserRepository userRepository;

    public UserProcedureDTO startProcedure(String userEmail, Long procedureId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable."));
        Procedure procedure = procedureRepository.findById(procedureId)
                .orElseThrow(() -> new IllegalArgumentException("Procédure introuvable : " + procedureId));

        UserProcedure userProcedure = new UserProcedure();
        userProcedure.setUser(user);
        userProcedure.setProcedure(procedure);
        userProcedure.setStatus(UserProcedureStatus.IN_PROGRESS);
        userProcedureRepository.save(userProcedure);

        return toDTO(userProcedure);
    }

    public List<UserProcedureDTO> getMyProcedures(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable."));
        return userProcedureRepository.findByUserId(user.getId())
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public UserProcedureDTO markStepCompleted(String userEmail, Long userProcedureId, Long stepId, boolean completed) {
        UserProcedure userProcedure = userProcedureRepository.findById(userProcedureId)
                .orElseThrow(() -> new IllegalArgumentException("Suivi de procédure introuvable."));

        // Sécurité : un utilisateur ne peut modifier que SON propre suivi
        if (!userProcedure.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("Ce suivi de procédure ne t'appartient pas.");
        }

        UserStepProgress progress = userStepProgressRepository
                .findByUserProcedureIdAndStepId(userProcedureId, stepId)
                .orElseGet(() -> {
                    UserStepProgress p = new UserStepProgress();
                    p.setUserProcedure(userProcedure);
                    ProcedureStep step = procedureStepRepository.findById(stepId)
                            .orElseThrow(() -> new IllegalArgumentException("Étape introuvable : " + stepId));
                    p.setStep(step);
                    return p;
                });

        progress.setCompleted(completed);
        progress.setCompletedAt(completed ? LocalDateTime.now() : null);
        userStepProgressRepository.save(progress);

        // Si toutes les étapes obligatoires sont cochées, on marque la procédure comme terminée
        List<ProcedureStep> allSteps = procedureStepRepository
                .findByProcedureIdOrderByStepNumberAsc(userProcedure.getProcedure().getId());
        List<UserStepProgress> allProgress = userStepProgressRepository.findByUserProcedureId(userProcedureId);

        boolean allRequiredDone = allSteps.stream()
                .filter(ProcedureStep::getRequired)
                .allMatch(step -> allProgress.stream()
                        .anyMatch(p -> p.getStep().getId().equals(step.getId()) && Boolean.TRUE.equals(p.getCompleted())));

        if (allRequiredDone && !allSteps.isEmpty()) {
            userProcedure.setStatus(UserProcedureStatus.COMPLETED);
            userProcedure.setCompletedAt(LocalDateTime.now());
            userProcedureRepository.save(userProcedure);
        }

        return toDTO(userProcedure);
    }

    private UserProcedureDTO toDTO(UserProcedure up) {
        UserProcedureDTO dto = new UserProcedureDTO();
        dto.setId(up.getId());
        dto.setProcedureId(up.getProcedure().getId());
        dto.setProcedureTitle(up.getProcedure().getTitle());
        dto.setStatus(up.getStatus());
        dto.setStartedAt(up.getStartedAt());
        dto.setCompletedAt(up.getCompletedAt());

        List<ProcedureStep> allSteps = procedureStepRepository
                .findByProcedureIdOrderByStepNumberAsc(up.getProcedure().getId());
        List<UserStepProgress> progressList = userStepProgressRepository.findByUserProcedureId(up.getId());

        List<StepProgressDTO> stepDTOs = allSteps.stream().map(step -> {
            boolean done = progressList.stream()
                    .anyMatch(p -> p.getStep().getId().equals(step.getId()) && Boolean.TRUE.equals(p.getCompleted()));
            return new StepProgressDTO(step.getId(), step.getTitle(), step.getStepNumber(), done);
        }).collect(Collectors.toList());

        dto.setSteps(stepDTOs);
        dto.setTotalSteps(allSteps.size());
        dto.setCompletedSteps((int) stepDTOs.stream().filter(StepProgressDTO::getCompleted).count());

        return dto;
    }
}
