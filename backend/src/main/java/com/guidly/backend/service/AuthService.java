package com.guidly.backend.service;

import com.guidly.backend.dto.AuthResponse;
import com.guidly.backend.dto.LoginRequest;
import com.guidly.backend.dto.RegisterRequest;
import com.guidly.backend.model.Role;
import com.guidly.backend.model.User;
import com.guidly.backend.repository.RoleRepository;
import com.guidly.backend.repository.UserRepository;
import com.guidly.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Un compte existe déjà avec cet email.");
        }

        // Rôle CITIZEN par défaut pour toute inscription publique.
        // Le rôle ADMIN ne se crée jamais via cet endpoint (sécurité).
        Role citizenRole = roleRepository.findByName("CITIZEN")
                .orElseThrow(() -> new IllegalStateException(
                        "Le rôle CITIZEN n'existe pas en base. Insère-le d'abord (voir instructions)."));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(citizenRole);
        user.setPreferredLanguage(request.getPreferredLanguage());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getName());
        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole().getName());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email ou mot de passe incorrect."));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getName());
        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole().getName());
    }
}
