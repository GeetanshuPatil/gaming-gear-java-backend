package com.geetanshu.gaminggearshop.controller;

import com.geetanshu.gaminggearshop.dto.AuthResponse;
import com.geetanshu.gaminggearshop.dto.LoginRequest;
import com.geetanshu.gaminggearshop.dto.RegisterRequest;
import com.geetanshu.gaminggearshop.entity.User;
import com.geetanshu.gaminggearshop.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 🔐 REGISTER API
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {

        return ResponseEntity.ok(authService.register(request));
    }
    // Login API
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }
}