// src/main/java/com/example/Sistema/de/Controle/Financeiro/Pessoal/dto/ResetPasswordRequest.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String token;
    private String newPassword;
}