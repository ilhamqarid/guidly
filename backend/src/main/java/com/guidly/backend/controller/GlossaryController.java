package com.guidly.backend.controller;

import com.guidly.backend.dto.GlossaryTermDTO;
import com.guidly.backend.service.GlossaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procedures")
public class GlossaryController {

    @Autowired
    private GlossaryService glossaryService;

    @GetMapping("/{id}/glossary")
    public List<GlossaryTermDTO> getGlossaryForProcedure(@PathVariable Long id) {
        return glossaryService.getTermsForProcedure(id);
    }
}
