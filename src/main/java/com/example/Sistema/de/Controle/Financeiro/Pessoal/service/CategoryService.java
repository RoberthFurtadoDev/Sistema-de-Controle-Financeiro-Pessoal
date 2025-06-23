// src/main/java/com/example/Sistema/de/Controle/Financeiro/Pessoal/service/CategoryService.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.service;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.CategoryDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.Category;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.enums.CategoryType; // Manter este import se CategoryType for usado
import com.example.Sistema.de.Controle.Financeiro.Pessoal.repository.CategoryRepository;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto, String userEmail) {
        User user = getUserByEmail(userEmail);
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setType(categoryDto.getType());
        category.setUser(user);
        Category savedCategory = categoryRepository.save(category);
        return mapToDto(savedCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoriesByUser(String userEmail) {
        User user = getUserByEmail(userEmail);
        return categoryRepository.findByUserOrderByNameAsc(user) // <--- MÉTODO CORRIGIDO AQUI
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto, String userEmail) {
        User user = getUserByEmail(userEmail);
        Category category = categoryRepository.findByIdAndUserId(categoryId, user.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada ou não pertence ao usuário"));
        category.setName(categoryDto.getName());
        category.setType(categoryDto.getType());
        Category updatedCategory = categoryRepository.save(category);
        return mapToDto(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long categoryId, String userEmail) {
        User user = getUserByEmail(userEmail);
        if (!categoryRepository.existsByIdAndUserId(categoryId, user.getId())) {
            throw new RuntimeException("Categoria não encontrada ou não pertence ao usuário");
        }
        categoryRepository.deleteById(categoryId);
    }

    private CategoryDto mapToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setType(category.getType());
        return dto;
    }
}