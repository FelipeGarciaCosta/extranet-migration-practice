package com.dsp.backend.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT que valida token y popula el SecurityContext.
 * Ajusta los nombres de los métodos de JwtUtils si en tu proyecto son distintos.
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                logger.info("Token JWT encontrado: {}", token);

                // --- <<<<<< AQUI: método usado para validar el token >>>>>>
                // En la mayoría de implementaciones el método se llama validateJwtToken(token).
                // Si tu clase JwtUtils usa otro nombre (validateToken / isTokenValid / isValid, etc.)
                // reemplaza la llamada por el nombre correcto.
                boolean valido = jwtUtils.validateJwtToken(token);

                if (valido) {
                    String username = jwtUtils.getUserNameFromJwtToken(token);
                    logger.info("Username extraído del token: {}", username);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("Usuario {} autenticado con éxito y contexto establecido.", username);
                    logger.debug("[SECURITY_DEBUG_AFTER_SET] SecurityContext.authentication = {}", SecurityContextHolder.getContext().getAuthentication());
                    logger.debug("[SECURITY_DEBUG_BEFORE_CHAIN] Antes de chain.doFilter: authentication = {}", SecurityContextHolder.getContext().getAuthentication());
                } else {
                    logger.debug("Token JWT inválido o expirado.");
                }
            } else {
                logger.debug("No se encontró cabecera Authorization Bearer.");
            }
        } catch (Exception e) {
            logger.error("Error en AuthTokenFilter: ", e);
            // No cortamos la cadena; si prefieres devolver 401 aquí, puedes hacer:
            // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            // return;
        }

        // Siempre continuar la cadena de filtros
        filterChain.doFilter(request, response);
        logger.debug("[SECURITY_DEBUG_AFTER_CHAIN] Después de chain.doFilter: authentication = {}", SecurityContextHolder.getContext().getAuthentication());
    }
}
