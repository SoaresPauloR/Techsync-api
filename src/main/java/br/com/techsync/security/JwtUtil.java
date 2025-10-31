package br.com.techsync.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Chave secreta utilizada para assinar e validar os tokens JWT
    private final String CHAVE_SECRETA = "MinhaChaveSuperSecreta1234567890123456";

    // Retorna a chave de assinatura baseada na chave secreta
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(CHAVE_SECRETA.getBytes());
    }

    // ================== Métodos ==================

    // Gera um token JWT para o email fornecido
    public String gerarToken(String email) {
        long tempoExpiracao = 1000 * 60 * 60; // 1 hora de validade

        return Jwts.builder()
                .setSubject(email) // Define o email do usuário como assunto do token
                .setIssuedAt(new Date()) // Data de criação do token
                .setExpiration(new Date(System.currentTimeMillis() + tempoExpiracao)) // Data de expiração
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Assina o token usando HS256
                .compact();
    }

    // Valida o token JWT e retorna o email contido nele
    public String validarToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Usa a chave para validar a assinatura
                .build()
                .parseClaimsJws(token) // Faz o parse do token JWT
                .getBody();

        return claims.getSubject(); // Retorna o email armazenado no token
    }
}

