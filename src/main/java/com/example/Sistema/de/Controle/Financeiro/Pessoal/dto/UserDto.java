// src/main/java/com/example/Sistema/de/Controle/Financeiro/Pessoal/dto/UserDto.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String username; // Nome de exibição
    private String email;
    // Não incluir senha aqui por segurança
}