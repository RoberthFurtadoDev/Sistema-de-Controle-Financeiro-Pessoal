// src/main/java/com/example/Sistema/de.Controle.Financeiro.Pessoal/controller/UserController.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.controller;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.UserDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal; // Para obter o usuário logado

@RestController
@RequestMapping("/api/users") // Endpoint base para operações de usuário
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint para obter o perfil do usuário logado
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(Principal principal) {
        UserDto userProfile = userService.getUserProfile(principal.getName());
        return ResponseEntity.ok(userProfile);
    }

    // Endpoint para atualizar o perfil do usuário logado
    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateUserProfile(@RequestBody UserDto userDto, Principal principal) {
        try {
            UserDto updatedProfile = userService.updateUserProfile(principal.getName(), userDto);
            return ResponseEntity.ok(updatedProfile);
        } catch (RuntimeException e) {
            // Retornar um erro mais específico se e-mail/username já estiver em uso
            if (e.getMessage().contains("em uso")) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT); // Status 409 Conflict
            }
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Status 400 Bad Request
        }
    }
}