package com.guidly.backend.controller;

import com.guidly.backend.dto.StartProcedureRequest;
import com.guidly.backend.dto.UserProcedureDTO;
import com.guidly.backend.service.UserProcedureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-procedures")
public class UserProcedureController {

    @Autowired
    private UserProcedureService userProcedureService;

    @PostMapping
    public ResponseEntity<?> startProcedure(@Valid @RequestBody StartProcedureRequest request, Authentication auth) {
        try {
            String email = auth.getName();
            return ResponseEntity.ok(userProcedureService.startProcedure(email, request.getProcedureId()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<UserProcedureDTO> getMyProcedures(Authentication auth) {
        return userProcedureService.getMyProcedures(auth.getName());
    }

    @PutMapping("/{id}/steps/{stepId}")
    public ResponseEntity<?> updateStep(@PathVariable Long id, @PathVariable Long stepId,
                                         @RequestParam(defaultValue = "true") boolean completed,
                                         Authentication auth) {
        try {
            return ResponseEntity.ok(userProcedureService.markStepCompleted(auth.getName(), id, stepId, completed));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
