package com.dsp.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsp.backend.dto.JwtResponse;
import com.dsp.backend.dto.LoginRequest;
import com.dsp.backend.security.jwt.JwtUtils;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils; // Servicio para crear el token (próximo paso)

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // 1. Autenticar: Spring Security verifica el usuario/contraseña
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // 2. Si la autenticación es exitosa, se establece el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 3. Generar el JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        // 4. Obtener detalles del usuario para la respuesta
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        java.util.List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(java.util.stream.Collectors.toList());

        // 5. Devolver la respuesta con el token (JSON)
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), roles));
    }
}