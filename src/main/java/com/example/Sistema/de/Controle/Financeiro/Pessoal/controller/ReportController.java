// src/main/java/com/example/Sistema/de/Controle/Financeiro/Pessoal/controller/ReportController.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.controller;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.BalanceDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.CategoryExpenseDto; // <--- Importar
import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.TransactionDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/balance")
    public ResponseEntity<BalanceDto> getUserBalance(Principal principal) {
        BalanceDto balance = transactionService.calculateUserBalance(principal.getName());
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/transactions-by-period")
    public ResponseEntity<List<TransactionDto>> getTransactionsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Principal principal) {

        List<TransactionDto> transactions = transactionService.getTransactionsByPeriod(principal.getName(), startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions-by-category/{categoryId}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByCategory(
            @PathVariable Long categoryId,
            Principal principal) {
        // Removido o try-catch local, pois o GlobalExceptionHandler cuidará das RuntimeExceptions.
        List<TransactionDto> transactions = transactionService.getTransactionsByCategory(principal.getName(), categoryId);
        return ResponseEntity.ok(transactions);
    }

    // --- NOVOS ENDPOINTS PARA GRÁFICOS ---
    @GetMapping("/expenses-by-category")
    public ResponseEntity<List<CategoryExpenseDto>> getExpensesByCategory(Principal principal) {
        List<CategoryExpenseDto> expenses = transactionService.getTotalExpensesByCategory(principal.getName());
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/incomes-by-category")
    public ResponseEntity<List<CategoryExpenseDto>> getIncomesByCategory(Principal principal) {
        List<CategoryExpenseDto> incomes = transactionService.getTotalIncomesByCategory(principal.getName());
        return ResponseEntity.ok(incomes);
    }
}