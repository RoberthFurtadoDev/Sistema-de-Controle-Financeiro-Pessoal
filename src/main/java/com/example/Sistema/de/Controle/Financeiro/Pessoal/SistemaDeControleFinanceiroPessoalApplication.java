package com.example.Sistema.de.Controle.Financeiro.Pessoal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SistemaDeControleFinanceiroPessoalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDeControleFinanceiroPessoalApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
