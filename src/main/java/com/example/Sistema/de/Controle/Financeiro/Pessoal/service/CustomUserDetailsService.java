package com.example.Sistema.de.Controle.Financeiro.Pessoal.service;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // Importe a interface
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService { // <<< CORREÇÃO AQUI

    @Autowired
    private UserRepository userRepository;

    @Override // <<< Garanta que esta anotação está presente
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o nome: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>() // Lista de autoridades/roles
        );
    }
}