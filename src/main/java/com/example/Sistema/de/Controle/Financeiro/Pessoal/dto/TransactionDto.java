package com.example.Sistema.de.Controle.Financeiro.Pessoal.dto;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.enums.CategoryType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransactionDto {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private Long categoryId; // Para criar/atualizar uma transação, recebemos o ID da categoria
    private String categoryName; // Para exibir uma transação, mostramos o nome da categoria
    private CategoryType type; // Para exibir o tipo (RECEITA/DESPESA)
}
