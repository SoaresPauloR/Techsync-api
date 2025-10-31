package br.com.techsync.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // Configura o CORS (Cross-Origin Resource Sharing) para a aplicação
    // Permite que requisições de qualquer origem acessem a API
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a todas as rotas da API
                .allowedOrigins("*") // Permite requisições de qualquer domínio
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos
                .allowedHeaders("*"); // Permite todos os cabeçalhos
    }
}
