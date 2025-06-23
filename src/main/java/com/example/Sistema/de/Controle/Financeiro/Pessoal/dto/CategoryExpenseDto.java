// src/main/java/com/example/Sistema/de/Controle/Financeiro/Pessoal/dto/CategoryExpenseDto.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.dto; // <--- GARANTA ESTE PACOTE

import lombok.AllArgsConstructor; // Para o construtor usado nas queries JPQL
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor // É importante para a query 'SELECT NEW ...' no TransactionRepository
public class CategoryExpenseDto {
    private String categoryName;
    private BigDecimal totalAmount;
}