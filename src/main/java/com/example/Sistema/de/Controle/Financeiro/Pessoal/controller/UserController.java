// src/main/java/com/example/Sistema/de.Controle.Financeiro.Pessoal/controller/UserController.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.controller;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.UserDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.ChangePasswordRequest; // <--- NOVO IMPORT
import com.example.Sistema.de.Controle.Financeiro.Pessoal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(Principal principal) {
        UserDto userProfile = userService.getUserProfile(principal.getName());
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateUserProfile(@RequestBody UserDto userDto, Principal principal) {
        try {
            UserDto updatedProfile = userService.updateUserProfile(principal.getName(), userDto);
            return ResponseEntity.ok(updatedProfile);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("em uso")) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // <--- NOVO ENDPOINT: Mudar a Senha do Usuário ---
    @PutMapping("/change-password") // Usamos PUT para uma atualização específica
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request, Principal principal) {
        try {
            userService.changePassword(principal.getName(), request);
            return ResponseEntity.ok("Senha alterada com sucesso!");
        } catch (RuntimeException e) {
            // Mensagens específicas para o frontend
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // Retorna a mensagem da exceção
        }
    }
}