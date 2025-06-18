# Sistema de Controle Financeiro Pessoal

Este é um projeto de API REST para um sistema de controle financeiro pessoal, desenvolvido com Java e Spring Boot.

## Funcionalidades

* Autenticação de usuários com JWT (JSON Web Tokens).
* CRUD completo para Usuários, Categorias e Transações.
* Relatórios de saldo financeiro.
* Integração com API externa para cotação de moedas.

## Tecnologias Utilizadas

* **Backend:** Java 21, Spring Boot 3
* **Segurança:** Spring Security, JWT
* **Banco de Dados:** PostgreSQL (Produção), H2 (Desenvolvimento)
* **Persistência:** Spring Data JPA, Hibernate
* **Build:** Apache Maven

## Como Rodar o Projeto Localmente

1.  **Pré-requisitos:**
    * Java 21
    * Maven
    * PostgreSQL

2.  **Clone o repositório:**
    ```bash
    git clone https://URL_DO_SEU_REPOSITORIO
    ```

3.  **Configure as variáveis de ambiente** no arquivo `application-prod.properties`.

4.  **Execute a aplicação:**
    ```bash
    mvn spring-boot:run
    ```

## Endpoints da API

### Autenticação

* `POST /api/auth/register`: Registra um novo usuário.
* `POST /api/auth/login`: Realiza o login e retorna um token JWT.

### Categorias

* `POST /api/categories`: Cria uma nova categoria (requer autenticação).
* `GET /api/categories`: Lista as categorias do usuário logado (requer autenticação).
* `PUT /api/categories/{id}`: Atualiza uma categoria (requer autenticação).
* `DELETE /api/categories/{id}`: Deleta uma categoria (requer autenticação).

### Transações

* `POST /api/transactions`: Cria uma nova transação (requer autenticação).
* `GET /api/transactions`: Lista as transações do usuário logado (requer autenticação).
* `PUT /api/transactions/{id}`: Atualiza uma transação (requer autenticação).
* `DELETE /api/transactions/{id}`: Deleta uma transação (requer autenticação).

### Relatórios

* `GET /api/reports/balance`: Retorna o saldo atual do usuário (requer autenticação).

### Moedas

* `GET /api/currency/rates/{baseCurrency}`: Retorna as cotações atuais com base em uma moeda (requer autenticação).
