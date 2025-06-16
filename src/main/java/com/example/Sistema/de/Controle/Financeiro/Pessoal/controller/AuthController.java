package com.example.Sistema.de.Controle.Financeiro.Pessoal.controller;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.LoginDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.RegisterDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        try {
            authService.registerUser(registerDto);
            return new ResponseEntity<>("Usuário registrado com sucesso!", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // NOVO ENDPOINT DE LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            String token = authService.loginUser(loginDto);
            // Retorna o token em um objeto JSON para facilitar o consumo no frontend
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (Exception e) {
            // Captura falhas de autenticação (usuário/senha inválidos)
            return new ResponseEntity<>("Usuário ou senha inválidos.", HttpStatus.UNAUTHORIZED);
        }
    }
}