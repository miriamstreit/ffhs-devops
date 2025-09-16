package ch.schuum.backend.auth;

import ch.schuum.backend.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Die Klasse WebSecurityConfig implementiert sämtliche Sicherheitskonfigurationen der Applikation.
 * Die restlichen Klassen in diesem Package werden hier zusammengeführt.
 *
 * @author Miriam Streit
 *
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class WebSecurityConfig {
    private final AuthEntryPointJwt unauthorizedHandler;

    /**
     * <p>erzeugt ein Bean für den Filter, der die JWT-Tokens prüft</p>
     * @return erzeugtes Bean vom Typ AuthTokenFilter
     * @author Miriam Streit
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * <p>erzeugt ein Bean für die Authentisierung, wobei der UserDetailsService und
     * der Passwort Encoder gesetzt werden</p>
     * @param http HTTP-Security Objekt zum Überschreiben
     * @param userDetailService UserDetailsService, der für die Authentisierung verwendet wird
     * @return erzeugtes Bean vom Typ AuthenticationManager
     * @throws Exception bei allen möglichen Problemen
     * @author Miriam Streit
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, UserDetailsServiceImpl userDetailService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    /**
     * <p>erzeugt ein Bean für den Passwort Encoder</p>
     * @return erzeugtes Bean vom Typ PasswordEncoder
     * @author Miriam Streit
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * <p>erzeugt ein Bean für den Abfrage-Filter, bei welchem alle sicherheitsrelevanten Aspekte geprüft werden</p>
     * <p>setzt den authenticationEntryPoint als Handler für alle Probleme, setzt Einstellungen zur Session und
     * gibt die Pfade vor, die geschützt oder ungeschützt sind</p>
     * @param http HTTP-Security Objekt zum Überschreiben
     * @return erzeugtes Bean vom Typ SecurityFilterChain
     * @throws Exception bei allen möglichen Problemen
     * @author Miriam Streit
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/auth/**").permitAll()
                .antMatchers("/v3/api-docs/**", "/v3/api-docs.yaml", "/swagger-ui/**", "/swagger-ui.html", "/actuator/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
