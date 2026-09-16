package com.guidly.backend.service;

import com.guidly.backend.dto.AssistantChatResponseDTO;
import com.guidly.backend.model.*;
import com.guidly.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Assistant CONTEXTUEL (section 10 du cahier des charges) : répond à des
 * questions sur UNE démarche précise déjà ouverte ("il me manque quoi ?",
 * "quelle est la prochaine étape ?", "pourquoi ce document ?").
 *
 * Différent de AiService (POST /api/ai/analyze) qui, lui, détecte l'intention
 * à partir d'une phrase libre pour trouver QUELLE démarche correspond.
 *
 * Principe strict (section 4/10) : ne répond QU'à partir des données déjà
 * en base pour cette procédure. Si la question ne correspond à aucun
 * pattern reconnu, on renvoie la phrase honnête définie dans le cahier
 * des charges plutôt que d'inventer une réponse.
 */
@Service
public class AssistantService {

    public static final String FALLBACK_TEXT = "Je ne dispose pas de cette information dans ma base actuelle.";

    @Autowired private ProcedureRepository procedureRepository;
    @Autowired private ProcedureStepRepository procedureStepRepository;
    @Autowired private ProcedureDocumentRepository procedureDocumentRepository;
    @Autowired private UserProcedureRepository userProcedureRepository;
    @Autowired private UserStepProgressRepository userStepProgressRepository;
    @Autowired private UserRepository userRepository;

    public AssistantChatResponseDTO answer(Long procedureId, String question, Authentication auth) {
        Procedure procedure = procedureRepository.findById(procedureId)
                .orElseThrow(() -> new IllegalArgumentException("Procédure introuvable : " + procedureId));

        List<ProcedureStep> steps = procedureStepRepository.findByProcedureIdOrderByStepNumberAsc(procedureId);
        List<ProcedureDocument> documents = procedureDocumentRepository.findByProcedureId(procedureId);

        // Si l'utilisateur est connecté ET suit déjà cette démarche, on peut
        // personnaliser la réponse avec sa vraie progression.
        UserProcedure userProcedure = findActiveUserProcedure(auth, procedureId);
        List<UserStepProgress> progress = userProcedure != null
                ? userStepProgressRepository.findByUserProcedureId(userProcedure.getId())
                : List.of();

        String q = normalize(question);
        String answer;

        if (q.contains("manque") || q.contains("reste") || q.contains("il me faut")) {
            answer = buildMissingDocumentsAnswer(documents, progress);
        } else if (q.contains("prochaine etape") || q.contains("suivant") || q.contains("maintenant") || q.contains("que dois je faire")) {
            answer = buildNextStepAnswer(steps, progress);
        } else if (q.contains("pourquoi")) {
            answer = buildWhyDocumentAnswer(documents, q);
        } else {
            answer = FALLBACK_TEXT;
        }

        return new AssistantChatResponseDTO(answer);
    }

    private UserProcedure findActiveUserProcedure(Authentication auth, Long procedureId) {
        if (auth == null || auth.getName() == null) return null;
        return userRepository.findByEmail(auth.getName())
                .flatMap(user -> userProcedureRepository.findByUserId(user.getId()).stream()
                        .filter(up -> up.getProcedure().getId().equals(procedureId))
                        .findFirst())
                .orElse(null);
    }

    private String buildMissingDocumentsAnswer(List<ProcedureDocument> documents, List<UserStepProgress> progress) {
        List<String> required = documents.stream()
                .filter(d -> Boolean.TRUE.equals(d.getRequired()))
                .map(d -> d.getDocument().getName())
                .collect(Collectors.toList());

        if (required.isEmpty()) {
            return "Aucun document obligatoire n'est enregistré pour cette démarche dans ma base actuelle.";
        }
        return "Les documents obligatoires pour cette démarche sont : " + String.join(", ", required) + ".";
    }

    private String buildNextStepAnswer(List<ProcedureStep> steps, List<UserStepProgress> progress) {
        if (steps.isEmpty()) {
            return "Aucune étape n'est enregistrée pour cette démarche dans ma base actuelle.";
        }

        Set<Long> doneStepIds = progress.stream()
                .filter(p -> Boolean.TRUE.equals(p.getCompleted()))
                .map(p -> p.getStep().getId())
                .collect(Collectors.toSet());

        Optional<ProcedureStep> next = steps.stream()
                .filter(s -> !doneStepIds.contains(s.getId()))
                .findFirst();

        if (next.isEmpty()) {
            return "Toutes les étapes de cette démarche sont déjà terminées.";
        }

        ProcedureStep step = next.get();
        return "La prochaine étape est : « " + step.getTitle() + " ». " + step.getDescription();
    }

    private String buildWhyDocumentAnswer(List<ProcedureDocument> documents, String normalizedQuestion) {
        Optional<ProcedureDocument> matched = documents.stream()
                .filter(d -> normalizedQuestion.contains(normalize(d.getDocument().getName())))
                .findFirst();

        if (matched.isEmpty()) {
            return FALLBACK_TEXT;
        }

        ProcedureDocument doc = matched.get();
        String description = doc.getDocument().getDescription();
        if (description != null && !description.isBlank()) {
            return description;
        }
        return "Ce document est requis pour cette démarche" +
                (doc.getCondition() != null ? " (" + doc.getCondition() + ")" : "") + ".";
    }

    private String normalize(String text) {
        if (text == null) return "";
        String lower = text.toLowerCase();
        String withoutAccents = Normalizer.normalize(lower, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return withoutAccents;
    }
}
