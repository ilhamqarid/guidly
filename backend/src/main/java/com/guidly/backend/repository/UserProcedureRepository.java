package com.guidly.backend.repository;

import com.guidly.backend.model.UserProcedure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProcedureRepository extends JpaRepository<UserProcedure, Long> {
    List<UserProcedure> findByUserId(Long userId);
}
