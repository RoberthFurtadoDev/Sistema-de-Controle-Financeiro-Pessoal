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
    private final AuthenticationManager authenticationManager; // NOVO
    private final JwtService jwtService; // NOVO

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, // NOVO
                       JwtService jwtService) { // NOVO
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public User registerUser(RegisterDto registerDto) {
        // ... seu código de registro existente ...
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

    // NOVO MÉTODO DE LOGIN
    public String loginUser(LoginDto loginDto) {
        // 1. Autentica o usuário usando o AuthenticationManager do Spring Security
        // Ele usará nosso CustomUserDetailsService e PasswordEncoder internamente
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );

        // 2. Se a autenticação for bem-sucedida, o objeto Authentication é preenchido.
        // Pegamos os detalhes do usuário autenticado.
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Geramos o token JWT para este usuário.
        return jwtService.generateToken(userDetails);
    }
}