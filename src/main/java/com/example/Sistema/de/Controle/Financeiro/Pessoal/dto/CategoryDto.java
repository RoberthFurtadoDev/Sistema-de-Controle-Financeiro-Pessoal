package com.example.Sistema.de.Controle.Financeiro.Pessoal.dto;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.enums.CategoryType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {
    private Long id;
    private String name;
    private CategoryType type;
}
