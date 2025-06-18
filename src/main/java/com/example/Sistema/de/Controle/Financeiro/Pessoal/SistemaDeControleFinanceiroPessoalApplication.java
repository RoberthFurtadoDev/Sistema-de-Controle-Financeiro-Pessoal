package com.example.Sistema.de.Controle.Financeiro.Pessoal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // Importe
import org.springframework.web.client.RestTemplate; // Importe

@SpringBootApplication
public class SistemaDeControleFinanceiroPessoalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDeControleFinanceiroPessoalApplication.class, args);
	}

	@Bean // Este método cria um Bean do RestTemplate que pode ser injetado em outros serviços
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
    