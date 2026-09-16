package com.guidly.backend.controller;

import com.guidly.backend.dto.ProcedureDTO;
import com.guidly.backend.dto.ProcedureDocumentDTO;
import com.guidly.backend.dto.ProcedureStepDTO;
import com.guidly.backend.dto.SourceDTO;
import com.guidly.backend.service.ProcedureDocumentService;
import com.guidly.backend.service.ProcedureService;
import com.guidly.backend.service.ProcedureStepService;
import com.guidly.backend.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procedures")
public class ProcedureController {

    @Autowired
    private ProcedureService procedureService;

    @Autowired
    private ProcedureStepService procedureStepService;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private ProcedureDocumentService procedureDocumentService;

    @GetMapping
    public List<ProcedureDTO> getAllProcedures() {
        return procedureService.getAllProcedures();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcedureDTO> getProcedureById(@PathVariable Long id) {
        return procedureService.getProcedureById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/steps")
    public List<ProcedureStepDTO> getSteps(@PathVariable Long id) {
        return procedureStepService.getStepsByProcedureId(id);
    }

    @GetMapping("/{id}/sources")
    public List<SourceDTO> getSources(@PathVariable Long id) {
        return sourceService.getSourcesByProcedureId(id);
    }

    @GetMapping("/{id}/documents")
    public List<ProcedureDocumentDTO> getDocuments(@PathVariable Long id) {
        return procedureDocumentService.getDocumentsByProcedureId(id);
    }
}
