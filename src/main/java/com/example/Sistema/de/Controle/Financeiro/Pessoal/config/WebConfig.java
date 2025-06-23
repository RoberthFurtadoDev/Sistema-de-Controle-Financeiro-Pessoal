// src/main/java/com/example.Sistema.de.Controle.Financeiro.Pessoal/config/WebConfig.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry; // Importar

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    // <--- CORREÇÃO AQUI: REGRAS DE REDIRECIONAMENTO PARA SPA MAIS ROBUSTAS --->
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redireciona a rota raiz para o index.html
        registry.addViewController("/").setViewName("forward:/index.html");

        // Para qualquer rota que NÃO seja uma API (/api/**) ou /h2-console/**
        // e que não tenha extensão de arquivo (para não pegar recursos estáticos como .js, .css, .png, etc.)
        // Redireciona para index.html para que o Angular Router lide com isso.
        registry.addViewController("/{path:[^\\.]*}")
                .setViewName("forward:/index.html");

        // Esta regra é mais abrangente para rotas aninhadas
        // (Spring Security pode interferir se não for bem específica)
        // O regex (?!api|h2-console) garante que não capture /api/ ou /h2-console/
        registry.addViewController("/{path:^(?!api|h2-console).*}/**")
                .setViewName("forward:/index.html");
    }
}