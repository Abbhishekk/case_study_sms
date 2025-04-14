package com.auth_service.controller;

import com.auth_service.DTO.LoginRequest;
import com.auth_service.DTO.RegisterRequest;
import com.auth_service.entity.Student;
import com.auth_service.repository.StudentRepository;
import com.auth_service.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerStudent_Success() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student student = invocation.getArgument(0);
            student.setStudentId(1L);
            return student;
        });

        // Act
        String result = authController.register(request);

        // Assert
        assertEquals("Registered successfully", result);
        verify(passwordEncoder).encode("password123");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void loginStudent_Success() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("password123");

        Student student = new Student();
        student.setStudentId(1L);
        student.setName("John Doe");
        student.setEmail(request.getEmail());
        student.setPassword("encodedPassword");

        when(studentRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(student));
        when(passwordEncoder.matches(request.getPassword(), student.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(student.getEmail())).thenReturn("generated.jwt.token");

        // Act
        String result = authController.login(request);

        // Assert
        assertEquals("generated.jwt.token", result);
        verify(studentRepository).findByEmail(request.getEmail());
        verify(passwordEncoder).matches(request.getPassword(), student.getPassword());
        verify(jwtUtil).generateToken(student.getEmail());
    }

    @Test
    void loginStudent_InvalidCredentials_WrongPassword() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("wrongPassword");

        Student student = new Student();
        student.setStudentId(1L);
        student.setName("John Doe");
        student.setEmail(request.getEmail());
        student.setPassword("encodedPassword");

        when(studentRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(student));
        when(passwordEncoder.matches(request.getPassword(), student.getPassword())).thenReturn(false);

        // Act
        String result = authController.login(request);

        // Assert
        assertEquals("Invalid credentials", result);
        verify(studentRepository).findByEmail(request.getEmail());
        verify(passwordEncoder).matches(request.getPassword(), student.getPassword());
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void registerStudent_PasswordIsEncoded() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student student = invocation.getArgument(0);
            assertEquals("encodedPassword", student.getPassword());
            return student;
        });

        // Act
        String result = authController.register(request);

        // Assert
        assertEquals("Registered successfully", result);
        verify(passwordEncoder).encode("password123");
    }
}