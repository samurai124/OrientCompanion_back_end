package org.example.orientcompanion.service;

import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.AuthResponse;
import org.example.orientcompanion.dto.LoginRequest;
import org.example.orientcompanion.dto.RegisterRequest;
import org.example.orientcompanion.entity.Admin;
import org.example.orientcompanion.entity.Counselor;
import org.example.orientcompanion.entity.Student;
import org.example.orientcompanion.entity.User;
import org.example.orientcompanion.exception.BusinessException;
import org.example.orientcompanion.exception.ResourceNotFoundException;
import org.example.orientcompanion.mapper.UserMapper;
import org.example.orientcompanion.repository.UserRepository;
import org.example.orientcompanion.security.JwtService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Transactional
    @CacheEvict(value = "users_email", key = "#request.email")
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Un compte existe déjà avec cet email");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = createUserInstance(request);

        user.setEmail(request.getEmail());
        user.setPasswordHash(encodedPassword);
        user.setFullName(request.getFullName());

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .token(token)
                .user(userMapper.toResponse(savedUser))
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = findByEmail(request.getEmail());
        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .user(userMapper.toResponse(user))
                .build();
    }

    @Cacheable(value = "users_email", key = "#email")
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + email));
    }

    @Cacheable(value = "users_id", key = "#id")
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'id : " + id));
    }

    private User createUserInstance(RegisterRequest request) {
        return switch (request.getRole()) {
            case STUDENT -> new Student();
            case COUNSELOR -> new Counselor();
            case ADMIN -> new Admin();
        };
    }
}