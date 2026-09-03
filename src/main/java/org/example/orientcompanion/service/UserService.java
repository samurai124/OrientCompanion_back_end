package org.example.orientcompanion.service;

import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.RegisterRequest;
import org.example.orientcompanion.entity.Admin;
import org.example.orientcompanion.entity.Counselor;
import org.example.orientcompanion.entity.Student;
import org.example.orientcompanion.entity.User;
import org.example.orientcompanion.exception.BusinessException;
import org.example.orientcompanion.exception.ResourceNotFoundException;
import org.example.orientcompanion.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Un compte existe déjà avec cet email");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = switch (request.getRole()) {
            case STUDENT -> Student.builder()
                    .email(request.getEmail())
                    .passwordHash(encodedPassword)
                    .fullName(request.getFullName())
                    .build();

            case COUNSELOR -> Counselor.builder()
                    .email(request.getEmail())
                    .passwordHash(encodedPassword)
                    .fullName(request.getFullName())
                    .build();

            case ADMIN -> Admin.builder()
                    .email(request.getEmail())
                    .passwordHash(encodedPassword)
                    .fullName(request.getFullName())
                    .build();
        };

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + email));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'id : " + id));
    }
}