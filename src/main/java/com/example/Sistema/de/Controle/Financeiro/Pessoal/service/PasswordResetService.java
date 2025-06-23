// src/main/java/com/example.Sistema.de.Controle.Financeiro.Pessoal/service/PasswordResetService.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.service;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.reset-token.expiry-minutes:30}")
    private long resetTokenExpiryMinutes;

    @Transactional
    public void createPasswordResetToken(String userEmail) {
        System.out.println("PasswordResetService: Requisição para e-mail: " + userEmail);
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("PasswordResetService: USUÁRIO ENCONTRADO: " + user.getEmail() + " (ID: " + user.getId() + ")");

            String token = UUID.randomUUID().toString();
            LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(resetTokenExpiryMinutes);

            user.setResetPasswordToken(token);
            user.setResetPasswordTokenExpiry(expiryDate);
            userRepository.save(user);

            String resetLink = frontendUrl + "/reset-password?token=" + token;
            String emailBody = "Olá " + user.getUsername() + ",\n\n"
                    + "Você solicitou a redefinição de sua senha. Use o link abaixo para redefinir:\n"
                    + resetLink + "\n\n"
                    + "Este link expirará em " + resetTokenExpiryMinutes + " minutos.\n"
                    + "Se você não solicitou isso, por favor, ignore este e-mail.\n\n"
                    + "Atenciosamente,\nSua Equipe de Suporte Financeiro.";

            emailService.sendSimpleEmail(user.getEmail(), "Redefinição de Senha - Sistema Financeiro", emailBody);
            System.out.println("PasswordResetService: EmailService.sendSimpleEmail() foi chamado com sucesso.");
        } else {
            System.out.println("PasswordResetService: E-MAIL NÃO ENCONTRADO NO BANCO DE DADOS: " + userEmail);
        }
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("Token de redefinição de senha inválido."));

        if (user.getResetPasswordTokenExpiry() != null && user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            user.setResetPasswordToken(null);
            user.setResetPasswordTokenExpiry(null);
            userRepository.save(user);
            throw new RuntimeException("Token de redefinição de senha expirado.");
        }

        // Criptografa e define a nova senha
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null); // Invalida o token após o uso
        user.setResetPasswordTokenExpiry(null);
        userRepository.save(user);

        // <--- NOVO: Enviar e-mail de notificação de mudança de senha ---
        String notificationSubject = "Sua senha foi alterada!";
        String notificationBody = "Olá " + user.getUsername() + ",\n\n"
                + "Sua senha para o Sistema Financeiro foi redefinida com sucesso.\n"
                + "Se você não realizou esta alteração, por favor, entre em contato conosco imediatamente.\n\n"
                + "Atenciosamente,\nSua Equipe de Suporte Financeiro.";
        emailService.sendSimpleEmail(user.getEmail(), notificationSubject, notificationBody);
        System.out.println("Email de notificação de senha alterada enviado para: " + user.getEmail());
    }
}