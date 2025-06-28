// src/main/java/com/example/Sistema/de/Controle/Financeiro/Pessoal/dto/ChangePasswordRequest.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}