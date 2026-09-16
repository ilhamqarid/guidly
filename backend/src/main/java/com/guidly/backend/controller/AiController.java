package com.guidly.backend.controller;

import com.guidly.backend.dto.AiAnalyzeRequestDTO;
import com.guidly.backend.dto.AiAnalyzeResponseDTO;
import com.guidly.backend.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    @PostMapping("/analyze")
    public ResponseEntity<AiAnalyzeResponseDTO> analyze(@RequestBody AiAnalyzeRequestDTO request) {
        if (request.getText() == null || request.getText().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(aiService.analyze(request.getText()));
    }
}
