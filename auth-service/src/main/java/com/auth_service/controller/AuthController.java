package com.auth_service.controller;

import com.auth_service.DTO.LoginRequest;
import com.auth_service.DTO.RegisterRequest;
import com.auth_service.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.auth_service.repository.StudentRepository;
import com.auth_service.security.JwtUtil;

import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private StudentRepository repository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        Student student = new Student();
        System.out.println("Request");
        System.out.println(request);
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        repository.save(student);
        return "Registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        System.out.println(request.getEmail());
        Optional<Student> studentOpt = repository.findByEmail(request.getEmail());
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (passwordEncoder.matches(request.getPassword(), student.getPassword())) {
                return jwtUtil.generateToken(student.getEmail());
            }
        }
        return "Invalid credentials";
    }
}
