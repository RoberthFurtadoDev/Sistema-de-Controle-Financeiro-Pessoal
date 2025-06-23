// src/main/java/com/example/Sistema/de/Controle/Financeiro/Pessoal/service/TransactionService.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.service;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.BalanceDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.CategoryExpenseDto; // <--- CORREÇÃO: Importar CategoryExpenseDto
import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.TransactionDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.Category;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.Transaction;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.repository.CategoryRepository;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.repository.TransactionRepository;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));
    }

    @Transactional
    public TransactionDto createTransaction(TransactionDto transactionDto, String userEmail) {
        User user = getUserByEmail(userEmail);
        Category category = categoryRepository.findByIdAndUserId(transactionDto.getCategoryId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada ou não pertence ao usuário"));
        Transaction transaction = new Transaction();
        transaction.setDescription(transactionDto.getDescription());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDate(transactionDto.getDate());
        transaction.setCategory(category);
        transaction.setUser(user);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToDto(savedTransaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByUser(String userEmail) {
        User user = getUserByEmail(userEmail);
        return transactionRepository.findByUserOrderByDateDesc(user)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionDto updateTransaction(Long transactionId, TransactionDto transactionDto, String userEmail) {
        User user = getUserByEmail(userEmail);
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, user.getId())
                .orElseThrow(() -> new RuntimeException("Transação não encontrada ou não pertence ao usuário"));
        Category category = categoryRepository.findByIdAndUserId(transactionDto.getCategoryId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada ou não pertence ao usuário"));
        transaction.setDescription(transactionDto.getDescription());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDate(transactionDto.getDate());
        transaction.setCategory(category);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToDto(updatedTransaction);
    }

    @Transactional
    public void deleteTransaction(Long transactionId, String userEmail) {
        User user = getUserByEmail(userEmail);
        if (!transactionRepository.existsByIdAndUserId(transactionId, user.getId())) {
            throw new RuntimeException("Transação não encontrada ou não pertence ao usuário");
        }
        transactionRepository.deleteById(transactionId);
    }

    @Transactional(readOnly = true)
    public BalanceDto calculateUserBalance(String userEmail) {
        User user = getUserByEmail(userEmail);
        BigDecimal balance = transactionRepository.calculateBalanceByUserId(user.getId())
                .orElse(BigDecimal.ZERO);
        return new BalanceDto(balance);
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByPeriod(String userEmail, LocalDate startDate, LocalDate endDate) {
        User user = getUserByEmail(userEmail);
        List<Transaction> transactions = transactionRepository.findByUserAndDateBetweenOrderByDateDesc(user, startDate, endDate);
        return transactions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByCategory(String userEmail, Long categoryId) {
        User user = getUserByEmail(userEmail);
        Category category = categoryRepository.findByIdAndUserId(categoryId, user.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada ou não pertence ao usuário"));
        List<Transaction> transactions = transactionRepository.findByUserAndCategoryOrderByDateDesc(user, category);
        return transactions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // --- NOVOS MÉTODOS PARA GRÁFICOS (Retornam CategoryExpenseDto) ---
    @Transactional(readOnly = true)
    public List<CategoryExpenseDto> getTotalExpensesByCategory(String userEmail) { // <--- userEmail aqui
        User user = getUserByEmail(userEmail);
        return transactionRepository.findTotalExpensesByCategory(user.getId());
    }

    @Transactional(readOnly = true)
    public List<CategoryExpenseDto> getTotalIncomesByCategory(String userEmail) { // <--- userEmail aqui
        User user = getUserByEmail(userEmail);
        return transactionRepository.findTotalIncomesByCategory(user.getId());
    }

    private TransactionDto mapToDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setDescription(transaction.getDescription());
        dto.setAmount(transaction.getAmount());
        dto.setDate(transaction.getDate());
        dto.setCategoryId(transaction.getCategory().getId());
        dto.setCategoryName(transaction.getCategory().getName());
        dto.setType(transaction.getCategory().getType());
        return dto;
    }
}