// src/main/java/com/example/Sistema/de/Controle/Financeiro/Pessoal/entity/User.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime; // <--- NOVO IMPORT para a data de expiração
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // --- NOVOS CAMPOS PARA REDEFINIÇÃO DE SENHA ---
    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "reset_password_token_expiry")
    private LocalDateTime resetPasswordTokenExpiry; // <--- NOVO: Data/hora de expiração do token

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Métodos para Category e Transaction (manter)
    public void addCategory(Category category) {
        this.categories.add(category);
        category.setUser(this);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.setUser(null);
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setUser(this);
    }

    public void removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setUser(null);
    }
}