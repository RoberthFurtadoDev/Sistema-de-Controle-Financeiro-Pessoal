// src/main/java/com/example/Sistema/de.Controle.Financeiro.Pessoal/service/AuthService.java
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
            System.out.println("Attempting authentication for email: " + loginDto.getEmail()); // Log de debug
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );
            System.out.println("Authentication successful for: " + authentication.getName()); // Log de debug
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            System.out.println("JWT Token generated: " + token); // Log de debug
            return token;
        } catch (org.springframework.security.core.AuthenticationException e) {
            // Captura exceções específicas de autenticação (BadCredentialsException, DisabledException, etc.)
            System.err.println("Authentication failed for " + loginDto.getEmail() + ": " + e.getMessage()); // Log de erro específico
            throw new RuntimeException("Credenciais inválidas. Verifique seu e-mail e senha.", e); // <--- Relança como RuntimeException tratável
        } catch (Exception e) {
            // Captura outras exceções inesperadas
            System.err.println("An unexpected error occurred during login for " + loginDto.getEmail() + ": " + e.getMessage()); // Log de erro inesperado
            throw new RuntimeException("Ocorreu um erro inesperado durante o login. Tente novamente.", e); // <--- Relança como RuntimeException
        }
    }
}