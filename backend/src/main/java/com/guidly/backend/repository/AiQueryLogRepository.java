package com.guidly.backend.repository;

import com.guidly.backend.model.AiQueryLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiQueryLogRepository extends JpaRepository<AiQueryLog, Long> {
}
