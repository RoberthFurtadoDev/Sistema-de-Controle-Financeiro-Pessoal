# Sistema de Controle Financeiro Pessoal - Backend

## 📖 Descrição

Este projeto é uma API RESTful completa para um sistema de controle financeiro pessoal, projetada para capacitar os usuários a gerenciar suas finanças de forma eficaz. A aplicação permite o registro de usuários, criação de categorias personalizadas de receitas e despesas, e o lançamento de transações detalhadas.

O backend foi construído com uma arquitetura robusta e escalável, utilizando tecnologias modernas do ecossistema Java e Spring, com foco em segurança, boas práticas de desenvolvimento e qualidade de código.

## ✨ Funcionalidades Principais

*   **Autenticação e Autorização:** Sistema seguro de registro e login com autenticação baseada em **JSON Web Tokens (JWT)**.
*   **CRUD de Usuários:** Cadastro e autenticação de usuários.
*   **CRUD de Categorias:** Usuários autenticados podem criar, listar, atualizar e deletar suas próprias categorias de receitas e despesas.
*   **CRUD de Transações:** Usuários autenticados podem gerenciar completamente suas transações financeiras, associando-as a categorias.
*   **Relatórios Financeiros:**
    *   Cálculo do saldo total em tempo real.
    *   Filtro de transações por período (data de início e fim).
    *   Filtro de transações por categoria específica.
*   **Integração com API Externa:** Consumo de uma API de terceiros para buscar cotações de moedas em tempo real.

## 🛠️ Decisões Técnicas e Arquitetura

Este projeto foi desenvolvido com base em decisões técnicas que visam as melhores práticas do mercado de desenvolvimento de software:

*   **Arquitetura em Camadas:** O código é organizado em uma arquitetura de 3 camadas (Controller, Service, Repository) para garantir a separação de responsabilidades, facilitar a manutenção e a testabilidade.
*   **Segurança Stateless com JWT:** Optamos por uma abordagem de segurança *stateless*. Após o login, o servidor não armazena o estado da sessão. Em vez disso, um token JWT é gerado e retornado ao cliente, que deve enviá-lo a cada requisição para endpoints protegidos. Isso torna a API mais escalável e ideal para ser consumida por diferentes tipos de clientes (web, mobile).
*   **Persistência de Dados com JPA/Hibernate:** Utilizamos o Spring Data JPA para abstrair e simplificar o acesso ao banco de dados, permitindo um desenvolvimento mais rápido e focado na lógica de negócios.
*   **DTO (Data Transfer Object) Pattern:** Para garantir uma separação clara entre a camada de persistência e a camada de API, utilizamos DTOs. Isso evita a exposição direta das entidades do banco de dados e nos dá controle total sobre os dados que são enviados e recebidos pela API.
*   **Banco de Dados Duplo (H2 e PostgreSQL):**
    *   **H2 (em memória):** Utilizado no perfil de desenvolvimento para agilidade, testes rápidos e para não exigir uma instalação de banco de dados localmente.
    *   **PostgreSQL:** Utilizado no perfil de produção para garantir a **persistência** dos dados, simulando um ambiente real. A troca entre os ambientes é gerenciada pelos Perfis do Spring Boot.
*   **Testes de Unidade:** Iniciamos a implementação de testes unitários com **JUnit 5 e Mockito** para validar a lógica de negócio na camada de serviço de forma isolada, garantindo a qualidade e a confiabilidade do código.

## 🚀 Tecnologias Utilizadas

| Categoria | Tecnologia |
| ----------------- | ---------------------------------------- |
| **Linguagem** | Java 21 |
| **Framework** | Spring Boot 3.x |
| **Segurança** | Spring Security 6, JSON Web Tokens (JWT) |
| **Persistência** | Spring Data JPA, Hibernate |
| **Banco de Dados**| PostgreSQL (Produção), H2 (Desenvolvimento) |
| **Build Tool** | Apache Maven |
| **Testes** | JUnit 5, Mockito |
| **API Testing** | Postman / Insomnia |

## ⚙️ Como Rodar o Projeto Localmente

### Pré-requisitos
*   JDK 21 ou superior
*   Apache Maven 3.6+
*   PostgreSQL instalado e rodando

### Passos para Execução
1.  **Clone o repositório:**
    bash
    git clone https://github.com/RoberthFurtadoDev/Sistema-de-Controle-Financeiro-Pessoal
    ```
2.  **Crie o Banco de Dados:**
    *   Abra o pgAdmin ou outro cliente PostgreSQL.
    *   Crie um novo banco de dados chamado `controle_financeiro_db`.

3.  **Configure as Variáveis de Ambiente:**
    *   Navegue até `src/main/resources/`.
    *   Renomeie o arquivo `application-prod.properties.example` para `application-prod.properties`.
    *   Abra o arquivo e insira a sua senha do PostgreSQL no campo `spring.datasource.password`.

4.  **Execute a Aplicação:**
    *   Abra um terminal na raiz do projeto.
    *   Para rodar com o banco de dados PostgreSQL, primeiro ative o perfil de produção no arquivo `application.properties` principal:
        ```properties
        spring.profiles.active=prod
        ```
    *   Execute o comando Maven para iniciar a aplicação:
        ```bash
        mvn spring-boot:run
        ```
    *   A API estará disponível em `http://localhost:8081`.

## ✅ Como Executar os Testes

Para rodar a suíte de testes unitários, execute o seguinte comando na raiz do projeto:
```bash
mvn test
```
---
Desenvolvido por: 💻 Roberth Furtado © 2025 | Todos os direitos reservados...