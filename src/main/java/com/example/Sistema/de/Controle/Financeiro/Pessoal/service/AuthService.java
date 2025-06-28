// src/main/java/com/example/Sistema.de.Controle.Financeiro.Pessoal/service/AuthService.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.service;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.LoginDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.RegisterDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public User registerUser(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new RuntimeException("Erro: Nome de usuário já está em uso!");
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("Erro: Email já está em uso!");
        }
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        return userRepository.save(user);
    }

    public String loginUser(LoginDto loginDto) {
        try {
            System.out.println("Attempting authentication for email: " + loginDto.getEmail());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(), // Usa e-mail para autenticar
                            loginDto.getPassword()
                    )
            );
            System.out.println("Authentication successful for: " + authentication.getName());

            // Após a autenticação, recuperamos o usuário completo pelo e-mail
            // para obter o nome de usuário (display name) que está na entidade User.
            User user = userRepository.findByEmail(loginDto.getEmail())
                    .orElseThrow(() -> new RuntimeException("Erro interno: Usuário autenticado não encontrado no DB."));

            // Passamos o NOME DE EXIBIÇÃO do usuário (user.getUsername()) e o UserDetails
            // para o JwtService gerar o token.
            String token = jwtService.generateToken(user.getUsername(), (UserDetails) authentication.getPrincipal()); // <--- CORREÇÃO AQUI

            System.out.println("JWT Token generated: " + token);
            return token;
        } catch (org.springframework.security.core.AuthenticationException e) {
            System.err.println("Authentication failed for " + loginDto.getEmail() + ": " + e.getMessage());
            throw new RuntimeException("Credenciais inválidas. Verifique seu e-mail e senha.", e);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during login for " + loginDto.getEmail() + ": " + e.getMessage());
            throw new RuntimeException("Ocorreu um erro inesperado durante o login. Tente novamente.", e);
        }
    }
}