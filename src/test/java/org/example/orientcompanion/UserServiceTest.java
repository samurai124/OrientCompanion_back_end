package org.example.orientcompanion;
import org.example.orientcompanion.dto.AuthResponse;
import org.example.orientcompanion.dto.LoginRequest;
import org.example.orientcompanion.dto.RegisterRequest;
import org.example.orientcompanion.dto.UserResponse;
import org.example.orientcompanion.entity.Admin;
import org.example.orientcompanion.entity.User;
import org.example.orientcompanion.enums.Role;
import org.example.orientcompanion.exception.BusinessException;
import org.example.orientcompanion.mapper.UserMapper;
import org.example.orientcompanion.repository.UserRepository;
import org.example.orientcompanion.security.JwtService;
import org.example.orientcompanion.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.Mock;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerRequest;
    private AuthResponse response;
    private Admin admin;
    private LoginRequest loginRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp(){
        registerRequest = new RegisterRequest();
        registerRequest.setFullName("Hamza Zaidi");
        registerRequest.setEmail("hamzazaidi@email.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole(Role.ADMIN);


        admin = new Admin();
        admin.setId(1L);
        admin.setFullName("Hamza Zaidi");
        admin.setEmail("hamzazaidi@email.com");
        admin.setPasswordHash("$2a$10$w8T9/M.gX940AUn14lX8eeE.M0S3jOqV0M9E48pI34/K0Hj7Xh5gC");


        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setFullName("Hamza zaidi");
        userResponse.setEmail("hamzazaidi@email.com");
        userResponse.setRole(Role.ADMIN);
        userResponse.setCreatedAt(LocalDateTime.now());



        response = new AuthResponse();
        response.setToken("fake-jwt-token");
        response.setUser(userResponse);


        loginRequest = new LoginRequest();
        loginRequest.setEmail("hamzazaidi@email.com");
        loginRequest.setPassword("password123");
    }


    @Test
    @DisplayName("Should successfully register a user and return AuthResponse with JWT token")
    void test_register_success() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("$2a$10$w8T9/M.gX940AUn14lX8eeE.M0S3jOqV0M9E48pI34/K0Hj7Xh5gC");
        when(userRepository.save(any(User.class))).thenReturn(admin);
        when(jwtService.generateToken(admin)).thenReturn("fake-jwt-token");
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        AuthResponse registred = userService.register(registerRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("fake-jwt-token");
        assertThat(response.getUser().getEmail()).isEqualTo("hamzazaidi@email.com");

        verify(passwordEncoder, times(1)).encode("password123");
        verify(jwtService, times(1)).generateToken(admin);
        verify(userRepository, times(1)).save(any(User.class));
    }



    @Test
    @DisplayName("Should throw BusinessException when registering with existing email")
    void register_DuplicateEmail_ThrowsException() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.register(registerRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Un compte existe déjà");

        verify(userRepository, never()).save(any());
        verify(jwtService, never()).generateToken(any());
    }


    @Test
    @DisplayName("Login successfully and return AuthResponse with JWT token")
    void login_test(){
        Authentication mockAuth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(admin));
        when(jwtService.generateToken(admin)).thenReturn("fake-jwt-token");
        when(userMapper.toResponse(admin)).thenReturn(userResponse);
        AuthResponse response = userService.login(loginRequest);
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("fake-jwt-token");

        verify(authenticationManager, times(1)).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );
        verify(jwtService, times(1)).generateToken(admin);
    }


    @Test
    @DisplayName("Should throw BadCredentialsException when credentials are wrong")
    void login_Failure_InvalidCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Identifiants invalides"));
        assertThatThrownBy(() -> userService.login(loginRequest))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Identifiants invalides");
        verify(authenticationManager, times(1)).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );
        verify(userRepository, never()).findByEmail(anyString());
        verify(jwtService, never()).generateToken(any());
        verify(userMapper, never()).toResponse(any());
    }







}
