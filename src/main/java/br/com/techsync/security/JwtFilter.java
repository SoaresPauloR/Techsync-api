package br.com.techsync.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    // Chave secreta usada para validar os tokens JWT
    private final String chaveSecreta = "MinhaChaveSuperSecreta1234567890123456";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Permite que requisições OPTIONS passem sem autenticação (necessário para CORS)
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remove o prefixo "Bearer "

            try {
                // Valida o token e extrai as informações (claims)
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(chaveSecreta.getBytes())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String email = claims.getSubject();
                request.setAttribute("email", email); // Adiciona o email na requisição para uso posterior

            } catch (Exception e) {
                // Token inválido ou expirado
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token JWT inválido ou expirado.");
                return;
            }

        } else {
            // Se o endpoint for protegido e não houver token, retorna 401
            if (isProtectedEndpoint(request)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token JWT ausente.");
                return;
            }
        }

        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }

    // Verifica se o endpoint requer autenticação
    private boolean isProtectedEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Login não precisa de token
        if (path.equals("/api/usuarios/login")) {
            return false;
        }

        // Cadastro (POST) não precisa de token
        if (path.equals("/api/usuarios") && method.equals("POST")) {
            return false;
        }

        // Todos os outros endpoints /api/usuarios exigem token
        return path.startsWith("/api/usuarios");
    }
}
