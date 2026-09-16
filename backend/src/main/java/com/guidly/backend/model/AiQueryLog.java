package com.guidly.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_query_logs")
@Getter
@Setter
@NoArgsConstructor
public class AiQueryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // nullable : peut être anonyme

    @Column(name = "input_text", nullable = false, columnDefinition = "TEXT")
    private String inputText;

    @Column(name = "detected_language", length = 10)
    private String detectedLanguage; // "fr", "en", "ar", "dar"

    @Column(name = "detected_intent", length = 100)
    private String detectedIntent;

    @Column(precision = 4, scale = 3)
    private BigDecimal confidence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matched_procedure_id")
    private Procedure matchedProcedure; // nullable

    @Column(name = "response_time_ms")
    private Integer responseTimeMs;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
