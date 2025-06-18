package com.example.Sistema.de.Controle.Financeiro.Pessoal.repository;

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

    // --- MANTENHA OS MÉTODOS QUE VOCÊ JÁ TEM ---
    List<Transaction> findByUserOrderByDateDesc(User user);
    Optional<Transaction> findByIdAndUserId(Long transactionId, Long userId);
    boolean existsByIdAndUserId(Long transactionId, Long userId);
    List<Transaction> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId")
    Optional<BigDecimal> calculateBalanceByUserId(@Param("userId") Long userId);

    // --- MÉTODO CORRIGIDO E ADICIONADO ---
    // Este é o método que o serviço vai chamar. O nome é claro e a query é gerada automaticamente.
    List<Transaction> findByUserAndCategoryOrderByDateDesc(User user, Category category);

    List<Transaction> findByUserAndCategoryId(User user, Long categoryId);
}
