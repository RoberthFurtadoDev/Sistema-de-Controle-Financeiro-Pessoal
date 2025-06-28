// src/main/java/com/example/Sistema/de.Controle.Financeiro.Pessoal/service/UserService.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.service;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.UserDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Método auxiliar para buscar usuário pelo e-mail (usado pelo Principal)
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email) // Já temos findByEmail no UserRepository
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

        // Validações de atualização:
        // 1. O ID no DTO deve corresponder ao ID do usuário autenticado.
        if (!user.getId().equals(updatedUserDto.getId())) {
            throw new RuntimeException("ID de usuário inválido para atualização.");
        }

        // 2. Não permitir mudança de email para um email já existente (se o email mudar)
        if (!user.getEmail().equals(updatedUserDto.getEmail()) && userRepository.existsByEmail(updatedUserDto.getEmail())) {
            throw new RuntimeException("Erro: O novo e-mail já está em uso por outro usuário.");
        }

        // 3. Não permitir mudança de username para um username já existente (se o username mudar)
        if (!user.getUsername().equals(updatedUserDto.getUsername()) && userRepository.existsByUsername(updatedUserDto.getUsername())) {
            throw new RuntimeException("Erro: O novo nome de usuário já está em uso por outro usuário.");
        }

        // Atualizar campos permitidos
        user.setUsername(updatedUserDto.getUsername());
        user.setEmail(updatedUserDto.getEmail());
        // A senha deve ser atualizada em um método/endpoint separado por segurança

        User savedUser = userRepository.save(user); // Salva as alterações
        UserDto resultDto = new UserDto();
        resultDto.setId(savedUser.getId());
        resultDto.setUsername(savedUser.getUsername());
        resultDto.setEmail(savedUser.getEmail());
        return resultDto;
    }

    // Método para alterar a senha (opcional, mas recomendado separadamente por segurança)
    // Precisaria de um DTO para oldPassword e newPassword, e PasswordEncoder
    // public void changePassword(String userEmail, ChangePasswordDto changePasswordDto) { ... }
}