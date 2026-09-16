package com.guidly.backend.service;

import com.guidly.backend.dto.AiAnalyzeResponseDTO;
import com.guidly.backend.dto.AiRawAnalysisDTO;
import com.guidly.backend.dto.ProcedureDTO;
import com.guidly.backend.model.AiQueryLog;
import com.guidly.backend.model.Procedure;
import com.guidly.backend.repository.AiQueryLogRepository;
import com.guidly.backend.repository.ProcedureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Orchestre le pipeline IA complet décrit en section 3 et 14 du prompt maître :
 *
 *   User → [Service Python : Intent + Entités] → Backend → Database (Retrieval)
 *        → Rules → Personalization → Réponse structurée
 *
 * Important (section 14 - "Bonne architecture") : le service Python ne fait
 * QUE comprendre le texte. C'est ICI, dans le backend, qu'on va chercher la
 * vraie procédure en base. Le LLM/matcher ne génère jamais le contenu
 * administratif lui-même.
 */
@Service
public class AiService {

    @Value("${ai.service.url:http://localhost:8000}")
    private String aiServiceUrl;

    @Autowired
    private ProcedureRepository procedureRepository;

    @Autowired
    private ProcedureService procedureService;

    @Autowired
    private AiQueryLogRepository aiQueryLogRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public AiAnalyzeResponseDTO analyze(String text) {
        long startTime = System.currentTimeMillis();

        AiRawAnalysisDTO raw = callAiService(text);

        AiAnalyzeResponseDTO response = new AiAnalyzeResponseDTO();
        response.setInput(raw.getInput());
        response.setLanguage(raw.getLanguage());
        response.setIntent(raw.getIntent());
        response.setIntentLabel(raw.getIntentLabel());
        response.setConfidence(raw.getConfidence());
        response.setAction(raw.getAction());
        response.setEntities(raw.getEntities());

        Procedure matchedProcedure = null;

        switch (raw.getAction()) {
            case "direct" -> {
                // Confiance >= 0.85 : Étape C - Procedure Retrieval directe
                Optional<Procedure> found = procedureRepository.findByIntentCode(raw.getIntent());
                if (found.isPresent()) {
                    matchedProcedure = found.get();
                    response.setMatchedProcedure(toProcedureDTO(matchedProcedure));
                    response.setMessage("Nous avons trouvé une démarche qui correspond à votre demande.");
                } else {
                    // L'IA a détecté une intention connue, mais elle n'existe pas
                    // (encore) en base. Principe fondamental (section 4) : ne
                    // jamais inventer. On repasse en clarification honnête.
                    response.setAction("clarify");
                    response.setMessage("Je ne dispose pas encore de cette démarche dans ma base actuelle.");
                }
            }
            case "suggest" -> {
                // Confiance moyenne : proposer 2-3 possibilités (section 28)
                List<ProcedureDTO> suggestions = new ArrayList<>();
                Optional<Procedure> topMatch = procedureRepository.findByIntentCode(raw.getIntent());
                topMatch.ifPresent(p -> suggestions.add(toProcedureDTO(p)));
                if (raw.getAlternatives() != null) {
                    raw.getAlternatives().forEach(alt -> {
                        if (alt.getIntent() != null) {
                            procedureRepository.findByIntentCode(alt.getIntent())
                                    .ifPresent(p -> suggestions.add(toProcedureDTO(p)));
                        }
                    });
                }
                response.setSuggestions(suggestions);
                response.setMessage("Pour mieux vous aider, voici les démarches qui pourraient correspondre :");
            }
            default -> {
                // "clarify" : confiance trop basse, ou hors périmètre (section 6/10)
                response.setMessage(
                        "Pour mieux vous aider, pouvez-vous préciser votre demande ? " +
                        "Si votre besoin ne concerne pas une démarche administrative, " +
                        "je ne dispose pas de cette information dans ma base actuelle."
                );
            }
        }

        long responseTimeMs = System.currentTimeMillis() - startTime;
        logQuery(text, raw, matchedProcedure, (int) responseTimeMs);

        return response;
    }

    private AiRawAnalysisDTO callAiService(String text) {
        try {
            var request = new java.util.HashMap<String, String>();
            request.put("text", text);
            return restTemplate.postForObject(aiServiceUrl + "/analyze", request, AiRawAnalysisDTO.class);
        } catch (RestClientException e) {
            // Le service Python est injoignable (pas démarré, mauvais port...).
            // On ne fait JAMAIS planter la requête pour l'utilisateur final :
            // on retombe proprement sur une clarification.
            AiRawAnalysisDTO fallback = new AiRawAnalysisDTO();
            fallback.setInput(text);
            fallback.setLanguage("unknown");
            fallback.setAction("clarify");
            fallback.setConfidence(0.0);
            return fallback;
        }
    }

    private void logQuery(String inputText, AiRawAnalysisDTO raw, Procedure matchedProcedure, int responseTimeMs) {
        AiQueryLog log = new AiQueryLog();
        log.setInputText(inputText);
        log.setDetectedLanguage(raw.getLanguage());
        log.setDetectedIntent(raw.getIntent());
        if (raw.getConfidence() != null) {
            log.setConfidence(BigDecimal.valueOf(raw.getConfidence()));
        }
        log.setMatchedProcedure(matchedProcedure);
        log.setResponseTimeMs(responseTimeMs);
        aiQueryLogRepository.save(log);
    }

    private ProcedureDTO toProcedureDTO(Procedure procedure) {
        // Réutilise la conversion déjà écrite dans ProcedureService pour ne
        // pas dupliquer la logique de mapping (DRY).
        return procedureService.getProcedureById(procedure.getId()).orElse(null);
    }
}
