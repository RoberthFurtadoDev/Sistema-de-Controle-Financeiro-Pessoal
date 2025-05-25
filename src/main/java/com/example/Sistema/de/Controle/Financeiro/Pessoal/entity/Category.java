package com.example.Sistema.de.Controle.Financeiro.Pessoal.entity;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.enums.CategoryType; // Importe o Enum
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING) // Diz ao JPA para armazenar o Enum como String no banco
    @Column(nullable = false, length = 20)
    private CategoryType type;

    // Relacionamento: Muitas Categorias pertencem a um Usuário
    // Este é o lado "dono" do relacionamento com User (User tem mappedBy="user")
    @ManyToOne(fetch = FetchType.LAZY) // LAZY é bom para performance
    @JoinColumn(name = "user_id", nullable = false) // Define a coluna de chave estrangeira
    private User user;

    // Relacionamento: Uma Categoria pode ter várias Transações
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    // Construtor customizado
    public Category(String name, CategoryType type, User user) {
        this.name = name;
        this.type = type;
        this.user = user;
    }

    // Métodos utilitários para gerenciar o relacionamento com Transaction (se necessário)
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setCategory(this);
    }

    public void removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setCategory(null);
    }
}