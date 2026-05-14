package com.geetanshu.gaminggearshop.service;

import com.geetanshu.gaminggearshop.dto.AuthResponse;
import com.geetanshu.gaminggearshop.dto.LoginRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.geetanshu.gaminggearshop.dto.RegisterRequest;
import com.geetanshu.gaminggearshop.entity.User;
import com.geetanshu.gaminggearshop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.geetanshu.gaminggearshop.util.JwtUtil;



@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {

        // 1. check if user exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        // 2. create user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // 🔐 encrypt password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole("USER");

        // 3. save to DB
        User savedUser = userRepository.save(user);

        // 4. generate JWT
        String token = jwtUtil.generateToken(savedUser.getEmail());

        // 5. return auth response
        return new AuthResponse(
                token,
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getName()
        );
    }

    public AuthResponse login(LoginRequest request) {

        // 1. find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // 2. check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // 🔐 generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        // return safe response
        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole(),
                user.getName()
        );
    }
}