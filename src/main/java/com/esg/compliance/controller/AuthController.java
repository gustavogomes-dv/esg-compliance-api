package com.esg.compliance.controller;

import com.esg.compliance.model.Usuario;
import com.esg.compliance.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(new TokenResponse(authService.login(request.email(), request.senha())));
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@RequestBody @Valid Usuario usuario) {
        return ResponseEntity.status(201).body(authService.registrar(usuario));
    }

    public record LoginRequest(String email, String senha) {}
    public record TokenResponse(String token) {}
}
