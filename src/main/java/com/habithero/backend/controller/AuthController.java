package com.habithero.backend.controller;


import com.habithero.backend.dto.JwtResponse;
import com.habithero.backend.dto.LoginRequest;
import com.habithero.backend.dto.RegisterRequest;
import com.habithero.backend.entity.User;
import com.habithero.backend.repository.UserRepository;
import com.habithero.backend.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req){
        if(userRepository.findByEmail(req.email()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","Email already exists"));
        }

        User u = User.builder()
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .xp(0)
                .level(1)
                .build();

        User saved = userRepository.save(u);

        return ResponseEntity.ok(Map.of("id", saved.getId(), "email", saved.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req){
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(req.email(),req.password());
        try{
            Authentication auth = authenticationManager.authenticate(authToken);

//            Authentication succeeded post this point.
            String token = jwtUtil.generateToken(req.email());
            return ResponseEntity.ok(new JwtResponse(token, "Bearer", jwtUtil.getSubject(token)));
        }catch(BadCredentialsException ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Invalid Credentials"));
        }
    }
}
