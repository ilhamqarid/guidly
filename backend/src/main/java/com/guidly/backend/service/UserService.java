package com.guidly.backend.service;

import com.guidly.backend.dto.ChangePasswordRequest;
import com.guidly.backend.dto.UpdateProfileRequest;
import com.guidly.backend.dto.UserDTO;
import com.guidly.backend.model.User;
import com.guidly.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public UserDTO getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable."));
        return toDTO(user);
    }

    public UserDTO updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable."));

        user.setName(request.getName());
        user.setPreferredLanguage(request.getPreferredLanguage());
        userRepository.save(user);

        return toDTO(user);
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable."));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Mot de passe actuel incorrect.");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(user.getName(), user.getEmail(), user.getRole().getName(), user.getPreferredLanguage());
    }
}
