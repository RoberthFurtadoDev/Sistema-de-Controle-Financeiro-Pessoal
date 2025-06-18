package com.example.Sistema.de.Controle.Financeiro.Pessoal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor // Cria um construtor que aceita o 'balance'
public class BalanceDto {
    private BigDecimal balance;
}
