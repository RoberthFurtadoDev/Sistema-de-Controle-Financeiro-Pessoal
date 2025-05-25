package com.example.Sistema.de.Controle.Financeiro.Pessoal.UserRepository;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.Category;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Encontra todas as categorias de um usuário específico
    List<Category> findByUser(User user);

    // Encontra todas as categorias de um usuário específico pelo ID do usuário
    List<Category> findByUserId(Long userId);

    // Encontra uma categoria específica de um usuário pelo nome da categoria e pelo usuário
    Optional<Category> findByNameAndUser(String name, User user);

    // Encontra uma categoria específica de um usuário pelo ID da categoria e pelo ID do usuário
    // Útil para garantir que um usuário só acesse/modifique suas próprias categorias
    Optional<Category> findByIdAndUserId(Long categoryId, Long userId);
}