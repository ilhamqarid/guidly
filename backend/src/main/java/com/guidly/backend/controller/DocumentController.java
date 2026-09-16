package com.guidly.backend.controller;

import com.guidly.backend.dto.DocumentDTO;
import com.guidly.backend.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping
    public List<DocumentDTO> getAllDocuments() {
        return documentService.getAllDocuments();
    }
}
