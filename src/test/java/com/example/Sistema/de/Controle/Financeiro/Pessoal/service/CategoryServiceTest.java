package com.example.Sistema.de.Controle.Financeiro.Pessoal.service;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.CategoryDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.Category;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.enums.CategoryType;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.repository.CategoryRepository;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito para este teste
class CategoryServiceTest {

    // Cria um "dublê" ou "mock" do repositório. Ele não acessa o banco de dados.
    // Nós controlamos o que ele faz.
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    // Cria uma instância real de CategoryService e injeta os mocks (@Mock) acima nela.
    @InjectMocks
    private CategoryService categoryService;

    private User user;
    private CategoryDto categoryDto;
    private Category category;

    // Este método roda antes de cada teste, preparando os objetos que vamos usar.
    @BeforeEach
    void setUp() {
        user = new User("testuser", "password", "test@example.com");
        user.setId(1L);

        categoryDto = new CategoryDto();
        categoryDto.setName("Salário");
        categoryDto.setType(CategoryType.RECEITA);

        category = new Category();
        category.setId(1L);
        category.setName("Salário");
        category.setType(CategoryType.RECEITA);
        category.setUser(user);
    }

    // A anotação @Test marca este método como um caso de teste.
    @Test
    void quandoCriarCategoria_deveRetornarCategoriaDto() {
        // --- Given (Dado) ---
        // Preparamos o cenário do teste.
        // Dizemos ao mock do userRepository: "quando o método findByUsername for chamado com 'testuser',
        // retorne um Optional contendo nosso objeto user."
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Dizemos ao mock do categoryRepository: "quando o método save for chamado com qualquer objeto Category,
        // retorne nosso objeto category."
        when(categoryRepository.save(any(Category.class))).thenReturn(category);


        // --- When (Quando) ---
        // Executamos a ação que queremos testar.
        CategoryDto resultDto = categoryService.createCategory(categoryDto, "testuser");


        // --- Then (Então) ---
        // Verificamos se o resultado é o esperado.
        assertNotNull(resultDto); // O resultado não deve ser nulo.
        assertEquals("Salário", resultDto.getName()); // O nome deve ser "Salário".
        assertEquals(CategoryType.RECEITA, resultDto.getType()); // O tipo deve ser RECEITA.
        assertEquals(1L, resultDto.getId()); // O ID deve ser 1.
    }
}
