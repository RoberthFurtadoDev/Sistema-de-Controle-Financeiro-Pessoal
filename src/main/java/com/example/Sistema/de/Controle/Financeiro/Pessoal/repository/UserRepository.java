// src/main/java/com/example/Sistema/de/Controle/Financeiro/Pessoal/repository/UserRepository.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.repository;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Optional<User> findByEmail(String email); // Método adicionado anteriormente
    Boolean existsByEmail(String email);

    // <--- NOVO MÉTODO: Encontrar usuário pelo token de redefinição de senha --->
    Optional<User> findByResetPasswordToken(String resetPasswordToken);
}