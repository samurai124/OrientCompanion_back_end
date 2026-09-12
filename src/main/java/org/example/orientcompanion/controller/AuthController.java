package org.example.orientcompanion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orientcompanion.dto.AuthResponse;
import org.example.orientcompanion.dto.LoginRequest;
import org.example.orientcompanion.dto.RegisterRequest;
import org.example.orientcompanion.dto.UserResponse;
import org.example.orientcompanion.entity.User;
import org.example.orientcompanion.mapper.UserMapper;
import org.example.orientcompanion.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Endpoints d'authentification et d'inscription (Publics / Utilisateur connecté)")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Inscription utilisateur", description = "Accès public. Permet de créer un compte STUDENT, COUNSELOR ou ADMIN.")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Connexion utilisateur", description = "Accès public. Authentifie l'utilisateur et retourne un token JWT ainsi que le rôle.")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Profil de l'utilisateur connecté", description = "Rôles autorisés: STUDENT, COUNSELOR, ADMIN. Retourne les informations et le rôle du compte actuellement authentifié.")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal User principal) {
        return ResponseEntity.ok(userMapper.toResponse(principal));
    }
}