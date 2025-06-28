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
                .allowedOrigins("http://localhost:4200") // Permite requisições vindas do seu frontend Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos
                .allowedHeaders("*") // Permite todos os cabeçalhos
                .allowCredentials(true); // Permite o envio de credenciais (como cookies ou tokens de autorização)
    }

    // <--- CORREÇÃO DEFINITIVA: REGRAS DE REDIRECIONAMENTO PARA SPA MAIS ROBUSTAS --->
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redireciona a rota raiz para o index.html
        registry.addViewController("/").setViewName("forward:/index.html");

        // Para qualquer rota que NÃO comece com /api/ ou /h2-console/,
        // e que não tenha extensão de arquivo (para não pegar .js, .css, .png, etc.),
        // e que não seja a raiz "/", redireciona para index.html.
        // O Angular Router no frontend cuidará do roteamento interno.
        registry.addViewController("/{path:[^\\.]*}") // Captura /reports, /dashboard, etc.
                .setViewName("forward:/index.html");

        // Esta é a regra mais abrangente para caminhos aninhados que não são APIs ou H2-Console
        registry.addViewController("/{path:^(?!api|h2-console).*$}/**") // Captura /reports/subpath, /dashboard/admin, etc.
                .setViewName("forward:/index.html");
    }
}