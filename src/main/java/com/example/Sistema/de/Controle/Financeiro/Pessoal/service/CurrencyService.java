package com.example.Sistema.de.Controle.Financeiro.Pessoal.service;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.CurrencyApiResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${exchangerate.api.key}")
    private String apiKey;

    private final String apiUrl = "https://v6.exchangerate-api.com/v6/";

    public CurrencyApiResponseDto getLatestRates(String baseCurrency) {
        // Monta a URL completa: https://v6.exchangerate-api.com/v6/SUA_CHAVE/latest/USD
        String finalUrl = apiUrl + apiKey + "/latest/" + baseCurrency;

        // Faz a requisição GET e mapeia a resposta JSON para o nosso DTO
        return restTemplate.getForObject(finalUrl, CurrencyApiResponseDto.class);
    }
}
    