package ch.schuum.backend.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Die Klasse WebConfig implementiert {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurer}
 * und überschreibt die existierende CORS Konfiguration. Dies ist notwendig, damit das Frontend von
 * einem anderen Port / einer anderen URL aus zugreifen kann.
 *
 * @author Miriam Streit
 *
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    /**
     * <p>überschreibt die CORS-Konfiguration</p>
     * @param registry bestehende CORS Einstellungen
     * @author Miriam Streit
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                .allowedOrigins("*");
    }
}