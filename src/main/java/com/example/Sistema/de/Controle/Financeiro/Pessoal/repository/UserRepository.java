package com.example.Sistema.de.Controle.Financeiro.Pessoal.repository;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA criará a implementação para este método automaticamente
    Optional<User> findByUsername(String username);

    // Método para verificar se um username já existe
    Boolean existsByUsername(String username);

    // Método para verificar se um email já existe
    Boolean existsByEmail(String email);
}