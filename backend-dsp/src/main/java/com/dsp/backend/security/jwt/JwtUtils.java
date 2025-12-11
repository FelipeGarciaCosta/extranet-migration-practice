package com.dsp.backend.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.dsp.backend.security.services.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    // Inyecta la clave secreta desde application.properties
    @Value("${extranet.app.jwtSecret}")
    private String jwtSecret;

    // Inyecta el tiempo de expiración desde application.properties
    @Value("${extranet.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // Método para obtener la clave de firma a partir de la cadena secreta
    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Genera el token JWT a partir de la autenticación del usuario.
     * @param authentication Objeto de autenticación de Spring Security.
     * @return El token JWT como String.
     */
    public String generateJwtToken(Authentication authentication) {

        // Obtiene el objeto principal (UserDetailsImpl) del usuario autenticado
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                // 1. Subject (quién es el usuario)
                .subject((userPrincipal.getUsername()))
                // 2. Fecha de emisión
                .issuedAt(new Date())
                // 3. Fecha de expiración
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                // 4. Algoritmo y firma
                .signWith(key(), Jwts.SIG.HS512)
                .compact();
    }

    /**
     * Obtiene el nombre de usuario a partir de un token JWT.
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Valida la firma y estructura del token.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.err.println("Token JWT inválido: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("Token JWT expirado: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("Token JWT no soportado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("El JWT está vacío: " + e.getMessage());
        }
        return false;
    }
}