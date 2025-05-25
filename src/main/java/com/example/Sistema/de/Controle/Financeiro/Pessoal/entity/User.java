package com.example.Sistema.de.Controle.Financeiro.Pessoal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // Define o nome da tabela no banco de dados como "users"
@Getter
@Setter
@NoArgsConstructor // Adiciona um construtor vazio (necessário para JPA)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incrementável gerenciado pelo banco
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100) // Para armazenar a senha hasheada
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // Relacionamento: Um Usuário pode ter várias Categorias.
    // 'mappedBy = "user"' indica que a entidade 'Category' tem um campo chamado 'user' que é o dono do relacionamento.
    // 'cascade = CascadeType.ALL' significa que operações (salvar, deletar, etc.) no User são propagadas para suas Categories.
    // 'orphanRemoval = true' remove categorias do banco se elas forem desassociadas deste usuário.
    // 'fetch = FetchType.LAZY' significa que as categorias não são carregadas automaticamente com o usuário, apenas quando acessadas.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Category> categories = new ArrayList<>();

    // Relacionamento: Um Usuário pode ter várias Transações.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    // Construtor customizado para criar instâncias de User mais facilmente (opcional)
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // --- Métodos Utilitários para gerenciar os relacionamentos bidirecionais ---
    // É uma boa prática ter esses métodos para garantir a consistência dos dados em ambos os lados do relacionamento.

    public void addCategory(Category category) {
        this.categories.add(category);
        category.setUser(this); // Garante que a categoria também saiba a qual usuário pertence
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.setUser(null); // Remove a associação
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setUser(this); // Garante que a transação também saiba a qual usuário pertence
    }

    public void removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setUser(null); // Remove a associação
    }
}