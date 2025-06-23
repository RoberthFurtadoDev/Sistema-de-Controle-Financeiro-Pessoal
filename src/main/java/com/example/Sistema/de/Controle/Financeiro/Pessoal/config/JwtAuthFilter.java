// src/main/java/com/example.Sistema.de.Controle.Financeiro.Pessoal/config/JwtAuthFilter.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.config;

import com.example.Sistema.de.Controle.Financeiro.Pessoal.service.CustomUserDetailsService;
import com.example.Sistema.de.Controle.Financeiro.Pessoal.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.core.AuthenticationException; // Importar
import org.springframework.security.authentication.BadCredentialsException; // Importar

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public JwtAuthFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Lida com CORS preflight (OPTIONS requests)
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
            return;
        }

        // Se o cabeçalho Authorization não existe ou não começa com "Bearer ", passa para o próximo filtro.
        // O SecurityFilterChain vai decidir se a rota é protegida e, se for, o AuthenticationEntryPoint será acionado.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); // Extrai o token JWT (remove "Bearer ")
        try {
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            // Se houver um erro ao extrair o nome de usuário (ex: token malformado ou corrompido),
            // lançamos uma exceção BadCredentialsException.
            System.err.println("Erro ao extrair username do JWT ou token inválido: " + e.getMessage());
            throw new BadCredentialsException("Token de autenticação inválido."); // <--- Lançar a exceção de verdade
        }

        // Se o nome de usuário foi extraído e o usuário ainda não está autenticado no contexto de segurança atual
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Valida o token JWT. Se for inválido, lança uma exceção.
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Se o token é válido, cria e define o contexto de segurança
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Credenciais (senha) não são armazenadas no token
                        userDetails.getAuthorities() // Papéis/autoridades do usuário
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context); // Define o contexto para a thread atual
            } else {
                // SE O TOKEN NÃO FOR VÁLIDO (EXPIRADO, ALTERADO, ETC.), LANÇAR EXCEÇÃO
                System.err.println("Token JWT para o usuário " + username + " é inválido ou expirado.");
                throw new BadCredentialsException("Token de autenticação expirado ou inválido."); // <--- Lançar a exceção de verdade
            }
        }
        // Continua a cadeia de filtros se a autenticação foi bem-sucedida ou se a requisição não precisa de autenticação aqui.
        filterChain.doFilter(request, response);
    }

    // SOBRESCREVER O MÉTODO shouldNotFilter PARA IGNORAR /api/auth/**
    // Isso garante que o JwtAuthFilter NÃO rode para as rotas de autenticação,
    // o que é crucial para que o login e registro funcionem sem um token.
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/");
    }
}