package com.bloggingplatformapi.service;

import com.bloggingplatformapi.dto.AuthResponse;
import com.bloggingplatformapi.dto.LoginRequest;
import com.bloggingplatformapi.dto.RegisterRequest;
import com.bloggingplatformapi.entity.Role;
import com.bloggingplatformapi.entity.User;
import com.bloggingplatformapi.exception.EmailAlreadyUsedException;
import com.bloggingplatformapi.repository.UserRepository;
import com.bloggingplatformapi.security.JwtService;
import com.bloggingplatformapi.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(
                userRepository,
                passwordEncoder,
                authenticationManager,
                jwtService
        );
    }

    @Test
    void register_shouldCreateUserWithUserRole() {
        RegisterRequest request = new RegisterRequest("USER@EXAMPLE.COM", "password123");
        User saved = user("user@example.com");

        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(saved);
        when(jwtService.generateToken("user@example.com", "USER")).thenReturn("token");

        AuthResponse response = authService.register(request);

        assertEquals("token", response.accessToken());
        assertEquals("user@example.com", response.email());
        assertEquals("USER", response.role());

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldRejectDuplicateEmail() {
        when(userRepository.existsByEmail("user@example.com")).thenReturn(true);

        assertThrows(
                EmailAlreadyUsedException.class,
                () -> authService.register(
                        new RegisterRequest("USER@EXAMPLE.COM", "password123")
                )
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldAuthenticateAndReturnToken() {
        LoginRequest request = new LoginRequest("USER@EXAMPLE.COM", "password123");
        User user = user("user@example.com");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken("user@example.com", "USER")).thenReturn("token");

        AuthResponse response = authService.login(request);

        assertEquals("token", response.accessToken());
        assertEquals("Bearer", response.tokenType());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    private User user(String email) {
        User user = new User();
        user.setEmail(email);
        user.setPassword("encoded");
        user.setRole(Role.USER);
        return user;
    }
}
