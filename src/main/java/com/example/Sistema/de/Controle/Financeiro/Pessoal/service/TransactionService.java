package com.example.Sistema.de.Controle.Financeiro.Pessoal.service;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.BalanceDto;
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

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    // --- MÉTODOS CRUD ---

    @Transactional
    public TransactionDto createTransaction(TransactionDto transactionDto, String username) {
        User user = getUserByUsername(username);
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
    public List<TransactionDto> getTransactionsByUser(String username) {
        User user = getUserByUsername(username);
        return transactionRepository.findByUserOrderByDateDesc(user)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionDto updateTransaction(Long transactionId, TransactionDto transactionDto, String username) {
        User user = getUserByUsername(username);
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
    public void deleteTransaction(Long transactionId, String username) {
        User user = getUserByUsername(username);
        if (!transactionRepository.existsByIdAndUserId(transactionId, user.getId())) {
            throw new RuntimeException("Transação não encontrada ou não pertence ao usuário");
        }
        transactionRepository.deleteById(transactionId);
    }

    // --- MÉTODOS DE RELATÓRIO ---

    @Transactional(readOnly = true)
    public BalanceDto calculateUserBalance(String username) {
        User user = getUserByUsername(username);
        BigDecimal balance = transactionRepository.calculateBalanceByUserId(user.getId())
                .orElse(BigDecimal.ZERO);
        return new BalanceDto(balance);
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByPeriod(String username, LocalDate startDate, LocalDate endDate) {
        User user = getUserByUsername(username);
        List<Transaction> transactions = transactionRepository.findByUserAndDateBetweenOrderByDateDesc(user, startDate, endDate);
        return transactions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactionsByCategory(String username, Long categoryId) {
        User user = getUserByUsername(username);
        Category category = categoryRepository.findByIdAndUserId(categoryId, user.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada ou não pertence ao usuário"));
        List<Transaction> transactions = transactionRepository.findByUserAndCategoryOrderByDateDesc(user, category);
        return transactions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // --- MÉTODO AUXILIAR ---

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
