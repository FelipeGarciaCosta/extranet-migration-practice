package com.dsp.backend.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.dsp.backend.security.jwt.AuthTokenFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthTokenFilter authTokenFilter;
    private final org.springframework.security.web.AuthenticationEntryPoint authenticationEntryPoint;

    @Value("${frontend.origin:http://localhost:4200}")
    private String frontendOrigin;

    public SecurityConfig(AuthTokenFilter authTokenFilter,
                          org.springframework.security.web.AuthenticationEntryPoint authenticationEntryPoint) {
        this.authTokenFilter = authTokenFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desactivar CSRF para API REST stateless
            .csrf(csrf -> csrf.disable())

            // CORS configurado por bean
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Stateless: no mantener HttpSession
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Usar RequestAttributeSecurityContextRepository para mantener el SecurityContext a nivel REQUEST
            .securityContext(sec -> sec.securityContextRepository(new RequestAttributeSecurityContextRepository()))

            // Manejo de excepciones (401)
            .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))

            // Reglas de autorización
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas (login / registro / test público)
                .requestMatchers("/auth/**", "/api/auth/**", "/api/test/publico").permitAll()

                // Permitir CORS preflight y estáticos
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/static/**", "/favicon.ico").permitAll()

                // El resto requiere autenticación
                .anyRequest().authenticated()
            );

        // Añadir filtro JWT antes de UsernamePasswordAuthenticationFilter
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS: permite llamadas desde el front (React).
     * Ajusta frontendOrigin en application.properties si hace falta.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200")); // Ambos frontends
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Bean obligatorio: PasswordEncoder.
     * BCrypt es la opción recomendada.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // puedes pasar strength si quieres, por ejemplo new BCryptPasswordEncoder(10);
    }

    /**
     * Exponer AuthenticationManager para los endpoints de login si lo necesitas.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
