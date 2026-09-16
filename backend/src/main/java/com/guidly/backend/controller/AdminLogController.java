package com.guidly.backend.controller;

import com.guidly.backend.dto.AiQueryLogDTO;
import com.guidly.backend.service.AiQueryLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Protégé par SecurityConfig (/api/admin/** exige ROLE_ADMIN) ET par
 * @PreAuthorize ici (défense en profondeur : la protection ne dépend plus
 * uniquement de l'ordre des requestMatchers dans SecurityConfig).
 */
@RestController
@RequestMapping("/api/admin/logs")
@PreAuthorize("hasRole('ADMIN')")
public class AdminLogController {

    @Autowired
    private AiQueryLogService aiQueryLogService;

    @GetMapping
    public List<AiQueryLogDTO> getRecentLogs() {
        return aiQueryLogService.getRecentLogs();
    }
}
