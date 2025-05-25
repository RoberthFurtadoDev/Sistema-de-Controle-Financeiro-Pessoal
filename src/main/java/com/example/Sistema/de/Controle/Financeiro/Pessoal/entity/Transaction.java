package com.example.Sistema.de.Controle.Financeiro.Pessoal.entity;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.enums.CategoryType; // Usaremos para definir o tipo da transação baseado na categoria
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate; // Ou LocalDateTime se precisar de hora

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2) // Boa precisão para valores monetários
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate date; // Data da transação

    // Não precisamos de um campo "type" (RECEITA/DESPESA) aqui se a Categoria já define isso.
    // O tipo da transação pode ser inferido pelo tipo da Categoria associada.

    // Relacionamento: Muitas Transações pertencem a um Usuário
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relacionamento: Muitas Transações pertencem a uma Categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Construtor customizado
    public Transaction(String description, BigDecimal amount, LocalDate date, User user, Category category) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.user = user;
        this.category = category;
    }

    // Método para obter o tipo da transação (RECEITA/DESPESA) a partir da categoria
    // Pode ser útil em DTOs ou na lógica de serviço.
    // Adicione @Transient se não quiser persistir este campo, mas sim calculá-lo.
    // No entanto, geralmente é melhor fazer essa lógica na camada de serviço ou DTO.
    // public CategoryType getTransactionType() {
    //     if (this.category != null) {
    //         return this.category.getType();
    //     }
    //     return null; // Ou lançar uma exceção se a categoria for obrigatória
    // }
}