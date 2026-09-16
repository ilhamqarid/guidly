package com.guidly.backend.controller;

import com.guidly.backend.dto.ProcedureDTO;
import com.guidly.backend.dto.ProcedureUpsertRequest;
import com.guidly.backend.service.ProcedureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints réservés au rôle ADMIN. Protégé par SecurityConfig
 * (/api/admin/** exige ROLE_ADMIN) ET par @PreAuthorize ici (défense en
 * profondeur, indépendante de l'ordre des règles dans SecurityConfig).
 * Création, modification et archivage des procédures.
 */
@RestController
@RequestMapping("/api/admin/procedures")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProcedureController {

    @Autowired
    private ProcedureService procedureService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ProcedureUpsertRequest request) {
        try {
            ProcedureDTO created = procedureService.createProcedure(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProcedureUpsertRequest request) {
        try {
            return procedureService.updateProcedure(id, request)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> archive(@PathVariable Long id) {
        boolean archived = procedureService.archiveProcedure(id);
        return archived ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
