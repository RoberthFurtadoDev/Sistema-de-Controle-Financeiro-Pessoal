package com.example.Sistema.de.Controle.Financeiro.Pessoal.UserRepository;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Opcional para interfaces que estendem JpaRepository, mas boa prática
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA criará a implementação para este método automaticamente
    // Ele buscará um usuário pelo username. Usado para o login.
    Optional<User> findByUsername(String username);

    // Método para verificar se um username já existe (útil no cadastro)
    Boolean existsByUsername(String username);

    // Método para verificar se um email já existe (útil no cadastro)
    Boolean existsByEmail(String email);

}