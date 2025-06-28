// src/main/java/com/example/Sistema/de.Controle.Financeiro.Pessoal/service/UserService.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.service;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.UserDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.ChangePasswordRequest; // <--- NOVO IMPORT
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder; // <--- NOVO IMPORT
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder; // <--- NOVO: Injetar PasswordEncoder
    @Autowired
    private EmailService emailService; // <--- NOVO: Injetar EmailService para notificação


    // Método auxiliar para buscar usuário pelo e-mail (usado pelo Principal)
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));
    }

    // Obter dados do usuário logado
    @Transactional(readOnly = true)
    public UserDto getUserProfile(String userEmail) {
        User user = getUserByEmail(userEmail);
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    // Atualizar dados do usuário logado
    @Transactional
    public UserDto updateUserProfile(String userEmail, UserDto updatedUserDto) {
        User user = getUserByEmail(userEmail);

        if (!user.getId().equals(updatedUserDto.getId())) {
            throw new RuntimeException("ID de usuário inválido para atualização.");
        }

        if (!user.getEmail().equals(updatedUserDto.getEmail()) && userRepository.existsByEmail(updatedUserDto.getEmail())) {
            throw new RuntimeException("Erro: O novo e-mail já está em uso por outro usuário.");
        }

        if (!user.getUsername().equals(updatedUserDto.getUsername()) && userRepository.existsByUsername(updatedUserDto.getUsername())) {
            throw new RuntimeException("Erro: O novo nome de usuário já está em uso por outro usuário.");
        }

        user.setUsername(updatedUserDto.getUsername());
        user.setEmail(updatedUserDto.getEmail());

        User savedUser = userRepository.save(user);
        UserDto resultDto = new UserDto();
        resultDto.setId(savedUser.getId());
        resultDto.setUsername(savedUser.getUsername());
        resultDto.setEmail(savedUser.getEmail());
        return resultDto;
    }

    // <--- NOVO MÉTODO: Mudar a Senha do Usuário ---
    @Transactional
    public void changePassword(String userEmail, ChangePasswordRequest request) {
        User user = getUserByEmail(userEmail);

        // 1. Verificar se a senha antiga está correta
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Senha antiga incorreta.");
        }

        // 2. Criptografar e definir a nova senha
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // 3. Enviar e-mail de notificação de mudança de senha
        String notificationSubject = "Sua senha foi alterada!";
        String notificationBody = "Olá " + user.getUsername() + ",\n\n"
                + "Sua senha para o Sistema Financeiro foi alterada com sucesso.\n"
                + "Se você não realizou esta alteração, por favor, entre em contato conosco imediatamente.\n\n"
                + "Atenciosamente,\nSua Equipe de Suporte Financeiro.";
        emailService.sendSimpleEmail(user.getEmail(), notificationSubject, notificationBody);
        System.out.println("Email de notificação de mudança de senha enviado para: " + user.getEmail());
    }
}