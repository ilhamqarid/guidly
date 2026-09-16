package com.guidly.backend.controller;

import com.guidly.backend.dto.ChangePasswordRequest;
import com.guidly.backend.dto.UpdateProfileRequest;
import com.guidly.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(Authentication auth) {
        return ResponseEntity.ok(userService.getProfile(auth.getName()));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest request, Authentication auth) {
        return ResponseEntity.ok(userService.updateProfile(auth.getName(), request));
    }

    @PutMapping("/me/password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request, Authentication auth) {
        try {
            userService.changePassword(auth.getName(), request);
            return ResponseEntity.ok("Mot de passe mis à jour.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
