package com.example.Webtoon.controller;

import com.example.Webtoon.model.User;
import com.example.Webtoon.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthControllerTest {

    private AuthController authController;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authController = new AuthController(userRepository, passwordEncoder);
    }

    @Test
    void testRegisterEncodesPasswordAndSavesUser() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("plainPassword");

        // Mock password encoder behavior
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        // Mock repository save behavior
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User savedUser = authController.register(user);

        // Assert
        assertEquals("encodedPassword", savedUser.getPassword(), "Password should be encoded before saving");
        assertEquals("testuser", savedUser.getUsername(), "Username should remain unchanged");

        // Verify interactions
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(savedUser);
    }
}