// src/main/java/com/example/Sistema/de.Controle.Financeiro.Pessoal/controller/AuthController.java
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
import java.util.Map; // <--- Importar

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

    // ENDPOINT DE LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            String token = authService.loginUser(loginDto);
            // Retorna o token em um objeto JSON
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (Exception e) {
            // CORREÇÃO: Retorna um objeto JSON com a mensagem de erro
            Map<String, String> errorResponse = Collections.singletonMap("message", "Usuário ou senha inválidos.");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED); // <--- CORREÇÃO AQUI: Retorna JSON
        }
    }
}