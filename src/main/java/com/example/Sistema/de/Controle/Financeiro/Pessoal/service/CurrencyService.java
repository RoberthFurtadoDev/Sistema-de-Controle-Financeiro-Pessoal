// src/main/java/com/example/Sistema/de/Controle/Financeiro/Pessoal/service/CurrencyService.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.service;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.CurrencyApiResponseDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.entity.User;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {

    @Value("${exchangerate.api.key}") // Chave da API externa
    private String apiKey;

    private final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    @Autowired
    private RestTemplate restTemplate; // <--- Injetar RestTemplate
    @Autowired
    private UserRepository userRepository; // <--- Injetar UserRepository

    // --- NOVO MÉTODO AUXILIAR: Buscar usuário por E-MAIL ---
    private User getUserByEmail(String email) { // <--- NOVO MÉTODO
        return userRepository.findByEmail(email) // <--- BUSCA POR EMAIL
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));
    }


    public CurrencyApiResponseDto getLatestRates(String baseCurrency, String userEmail) { // <--- userEmail agora
        // Embora a API externa não use o usuário, o método do serviço o recebe
        // para consistência se no futuro a API for por usuário.
        User user = getUserByEmail(userEmail); // <--- USAR NOVO MÉTODO (mesmo que não usado na lógica da API externa)

        String url = BASE_URL + apiKey + "/latest/" + baseCurrency;
        return restTemplate.getForObject(url, CurrencyApiResponseDto.class);
    }
}