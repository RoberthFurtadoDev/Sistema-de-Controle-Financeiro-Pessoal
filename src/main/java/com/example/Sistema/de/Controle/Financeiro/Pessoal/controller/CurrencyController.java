package com.example.Sistema.de.Controle.Financeiro.Pessoal.controller;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.dto.CurrencyApiResponseDto;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    // Endpoint para buscar as cotações com base em uma moeda. Ex: /api/currency/rates/BRL
    @GetMapping("/rates/{baseCurrency}")
    public ResponseEntity<CurrencyApiResponseDto> getLatestRates(@PathVariable String baseCurrency) {
        CurrencyApiResponseDto rates = currencyService.getLatestRates(baseCurrency.toUpperCase());
        return ResponseEntity.ok(rates);
    }
}
    