package com.example.Sistema.de.Controle.Financeiro.Pessoal.security; // ou .security

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest; // Para H2 console

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/h2-console/**").permitAll() // Permite acesso ao H2 Console sem autenticação
                                // .requestMatchers(PathRequest.toH2Console()).permitAll() // Alternativa mais robusta para H2 Console
                                .anyRequest().authenticated() // Todas as outras requisições exigem autenticação
                )
                .formLogin(formLogin -> // Configura o formulário de login padrão
                        formLogin
                                .loginPage("/login") // Se você tiver uma página de login customizada
                                .permitAll()
                )
                .logout(logout -> // Configura o logout
                        logout
                                .logoutSuccessUrl("/login?logout")
                                .permitAll()
                )
                .csrf(csrf -> csrf
                                .ignoringRequestMatchers("/h2-console/**") // Desabilita CSRF para o H2 Console
                        // .ignoringRequestMatchers(PathRequest.toH2Console()) // Alternativa mais robusta
                )
                .headers(headers -> headers
                                .frameOptions(frameOptions -> frameOptions.sameOrigin()) // Necessário para o H2 Console funcionar em um frame
                        // .frameOptions(frameOptions -> frameOptions.disable()) // Alternativa mais simples para H2, mas menos segura em geral
                );

        return http.build();
    }

    // Você adicionará aqui a configuração do PasswordEncoder e UserDetailsService mais tarde
    // para o login com usuários do banco.
}