// src/main/java/com/example/Sistema/de/Controle/Financeiro/Pessoal/repository/TransactionRepository.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.repository;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.CategoryExpenseDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.Category;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.Transaction;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserOrderByDateDesc(User user);
    Optional<Transaction> findByIdAndUserId(Long transactionId, Long userId);
    boolean existsByIdAndUserId(Long transactionId, Long userId);
    List<Transaction> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(CASE WHEN c.type = 'RECEITA' THEN t.amount ELSE -t.amount END) " +
            "FROM Transaction t JOIN t.category c WHERE t.user.id = :userId")
    Optional<BigDecimal> calculateBalanceByUserId(@Param("userId") Long userId);

    List<Transaction> findByUserAndCategoryOrderByDateDesc(User user, Category category);
    List<Transaction> findByUserAndCategoryId(User user, Long categoryId);

    // --- NOVO MÉTODO PARA GRÁFICOS: Despesas Totais por Categoria ---
    @Query("SELECT NEW com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.CategoryExpenseDto(c.name, SUM(t.amount)) " +
            "FROM Transaction t JOIN t.category c " +
            "WHERE t.user.id = :userId AND c.type = 'DESPESA' " + // Filtrar apenas despesas
            "GROUP BY c.name ORDER BY SUM(t.amount) DESC")
    List<CategoryExpenseDto> findTotalExpensesByCategory(@Param("userId") Long userId);

    // --- NOVO MÉTODO PARA GRÁFICOS: Receitas Totais por Categoria ---
    @Query("SELECT NEW com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.CategoryExpenseDto(c.name, SUM(t.amount)) " +
            "FROM Transaction t JOIN t.category c " +
            "WHERE t.user.id = :userId AND c.type = 'RECEITA' " + // Filtrar apenas receitas
            "GROUP BY c.name ORDER BY SUM(t.amount) DESC")
    List<CategoryExpenseDto> findTotalIncomesByCategory(@Param("userId") Long userId);
}