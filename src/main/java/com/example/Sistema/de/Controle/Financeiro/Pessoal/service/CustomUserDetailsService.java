// src/main/java/com/example/Sistema/de/Controle/Financeiro/Pessoal/service/CustomUserDetailsService.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.service;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // <--- PARAMETRO É EMAIL
        // Busca o usuário pelo email
        User user = userRepository.findByEmail(email) // <--- USAR findByEmail AQUI
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));

        // Retorna um UserDetails do Spring Security.
        // O primeiro parâmetro (username) DEVE ser o e-mail que foi usado para buscar,
        // pois é isso que o Spring Security vai comparar com o que foi digitado no campo "username" do AuthenticationToken.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // <--- USAR user.getEmail() AQUI
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}