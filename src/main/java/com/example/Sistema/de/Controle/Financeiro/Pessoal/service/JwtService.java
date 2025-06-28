// src/main/java/com/example/Sistema.de.Controle.Financeiro.Pessoal/service/JwtService.java
package com.example.Sistema.de.Controle.Financeiro.Pessoal.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm; // Importar
// REMOVIDO: import io.jsonwebtoken.io.Decoders; // Não é usado em JJWT 0.9.1
// REMOVIDO: import io.jsonwebtoken.security.Keys; // Não é usado em JJWT 0.9.1
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec; // Usado para SecretKey
import java.security.Key; // Importar
import java.util.Base64; // Importar Base64
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_KEY_STRING;
    private final long EXPIRATION_TIME;
    private final Key SIGNING_KEY;

    public JwtService(@Value("${jwt.secret}") String secretKeyString,
                      @Value("${jwt.expiration}") long expirationTime) {
        this.SECRET_KEY_STRING = secretKeyString;
        this.EXPIRATION_TIME = expirationTime;
        byte[] keyBytes = Base64.getDecoder().decode(secretKeyString);
        this.SIGNING_KEY = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName()); // Usado SecretKeySpec
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String subjectFromToken = extractUsername(token);
        final String userEmailFromUserDetails = userDetails.getUsername();

        return (subjectFromToken.equals(userEmailFromUserDetails) && !isTokenExpired(token));
    }

    public String generateToken(String usernameDisplay, UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("display_name", usernameDisplay);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY) // <--- CORREÇÃO AQUI: Ordem dos parâmetros
                .compact();
    }
}