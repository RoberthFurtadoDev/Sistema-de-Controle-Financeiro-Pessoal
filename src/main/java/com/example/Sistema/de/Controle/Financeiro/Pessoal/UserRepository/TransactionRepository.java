package com.example.Sistema.de.Controle.Financeiro.Pessoal.UserRepository;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.Category;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.Transaction;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Encontra todas as transações de um usuário específico
    List<Transaction> findByUserOrderByDateDesc(User user); // Ordena pela data mais recente

    // Encontra todas as transações de um usuário específico pelo ID do usuário
    List<Transaction> findByUserIdOrderByDateDesc(Long userId);

    // Encontra transações de um usuário dentro de um período específico
    List<Transaction> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate startDate, LocalDate endDate);
    List<Transaction> findByUserIdAndDateBetweenOrderByDateDesc(Long userId, LocalDate startDate, LocalDate endDate);

    // Encontra transações de um usuário para uma categoria específica
    List<Transaction> findByUserAndCategoryOrderByDateDesc(User user, Category category);
    List<Transaction> findByUserIdAndCategoryIdOrderByDateDesc(Long userId, Long categoryId);

    // Exemplo de consulta customizada com @Query para calcular o saldo total de um usuário
    // (Essa lógica de saldo pode ficar melhor na camada de Serviço, mas aqui é um exemplo de @Query)
    @Query("SELECT SUM(CASE WHEN t.category.type = com.example.Sistema.de.Controle.Financeiro.Pessoal.enums.CategoryType.RECEITA THEN t.amount ELSE -t.amount END) " +
            "FROM Transaction t WHERE t.user = :user")
    Optional<BigDecimal> calculateBalanceByUser(@Param("user") User user);

    @Query("SELECT SUM(CASE WHEN t.category.type = com.example.Sistema.de.Controle.Financeiro.Pessoal.enums.CategoryType.RECEITA THEN t.amount ELSE -t.amount END) " +
            "FROM Transaction t WHERE t.user.id = :userId")
    Optional<BigDecimal> calculateBalanceByUserId(@Param("userId") Long userId);
}