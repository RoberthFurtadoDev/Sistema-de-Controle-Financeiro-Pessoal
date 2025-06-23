// src/main/java/com/example/Sistema/de/Controle/Financeiro/Pessoal/controller/PasswordResetController.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.controller;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.ForgotPasswordRequest;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.ResetPasswordRequest;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    // Endpoint para solicitar a redefinição de senha (envia e-mail)
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            passwordResetService.createPasswordResetToken(request.getEmail());
            // Mensagem genérica por segurança (para não revelar se o e-mail existe)
            return ResponseEntity.ok("Se o e-mail estiver cadastrado, você receberá instruções para redefinir sua senha.");
        } catch (RuntimeException e) {
            // Captura RuntimeExceptions do serviço (ex: email não encontrado, embora o serviço não lance para este caso)
            System.err.println("Erro ao processar solicitação de esqueci senha: " + e.getMessage());
            return new ResponseEntity<>("Ocorreu um erro ao processar sua solicitação. Tente novamente mais tarde.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para redefinir a senha com o token (será usado na tela ResetPasswordComponent no frontend)
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok("Senha redefinida com sucesso! Você já pode fazer login com a nova senha.");
        } catch (RuntimeException e) {
            // Captura RuntimeExceptions do serviço (token inválido/expirado)
            System.err.println("Erro ao redefinir senha: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}