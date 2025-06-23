// src/main/java/com/example/Sistema/de/Controle/Financeiro/Pessoal/repository/CategoryRepository.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.repository;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.Category;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User; // Importar User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Método para encontrar categorias por usuário, ordenadas por nome
    List<Category> findByUserOrderByNameAsc(User user); // <--- MÉTODO ADICIONADO AQUI

    // Método para encontrar categoria por ID e ID do usuário
    Optional<Category> findByIdAndUserId(Long id, Long userId);

    // Método para verificar se uma categoria existe por ID e ID do usuário
    Boolean existsByIdAndUserId(Long id, Long userId);
}