package com.example.Sistema.de.Controle.Financeiro.Pessoal.controller;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.CategoryDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // CRIAR: POST /api/categories
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto, Principal principal) {
        CategoryDto createdCategory = categoryService.createCategory(categoryDto, principal.getName());
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    // LER: GET /api/categories
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategoriesByUser(Principal principal) {
        List<CategoryDto> categories = categoryService.getCategoriesByUser(principal.getName());
        return ResponseEntity.ok(categories);
    }

    // ATUALIZAR: PUT /api/categories/{id}
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto, Principal principal) {
        try {
            CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto, principal.getName());
            return ResponseEntity.ok(updatedCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETAR: DELETE /api/categories/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id, Principal principal) {
        try {
            categoryService.deleteCategory(id, principal.getName());
            return ResponseEntity.noContent().build(); // Status 204
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
