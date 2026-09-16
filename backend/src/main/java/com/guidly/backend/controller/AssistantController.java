package com.guidly.backend.controller;

import com.guidly.backend.dto.AssistantChatRequestDTO;
import com.guidly.backend.dto.AssistantChatResponseDTO;
import com.guidly.backend.service.AssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assistant")
public class AssistantController {

    @Autowired
    private AssistantService assistantService;

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody AssistantChatRequestDTO request, Authentication auth) {
        try {
            AssistantChatResponseDTO response = assistantService.answer(
                    request.getProcedureId(), request.getQuestion(), auth
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
