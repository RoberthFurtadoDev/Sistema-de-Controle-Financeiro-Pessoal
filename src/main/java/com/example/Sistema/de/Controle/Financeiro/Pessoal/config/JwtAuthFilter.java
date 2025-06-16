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
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final SecurityContextRepository securityContextRepository;

    /**
     * Construtor modificado para injeção de dependências.
     * O Spring fornecerá as instâncias de JwtService e CustomUserDetailsService.
     * Nós mesmos criamos a instância do repositório de contexto padrão.
     */
    @Autowired
    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.securityContextRepository = new RequestAttributeSecurityContextRepository();
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

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        try {
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            // Se houver um erro ao extrair o nome de usuário (ex: token expirado),
            // simplesmente passamos para o próximo filtro. A segurança não será aplicada.
            filterChain.doFilter(request, response);
            return;
        }

        // Verifica se o usuário não está autenticado ainda
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Cria um novo contexto de segurança vazio
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                // Cria o token de autenticação
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Define a autenticação no novo contexto
                context.setAuthentication(authToken);

                // Define o novo contexto no SecurityContextHolder (para a thread atual)
                SecurityContextHolder.setContext(context);

                // **A SOLUÇÃO DEFINITIVA:**
                // Salva o contexto no repositório para que ele seja propagado
                // para os próximos filtros da cadeia de segurança.
                this.securityContextRepository.saveContext(context, request, response);
            }
        }
        filterChain.doFilter(request, response);
    }
}