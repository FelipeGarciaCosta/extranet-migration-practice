package com.dsp.backend.test;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    // Endpoint público (no requiere token si en SecurityConfig está permitAll)
    @GetMapping("/publico")
    public String publicAccess() {
        return "Contenido Público (accesible sin autenticación). Probando";
    }

    // Solo usuarios con ROLE_ADMIN pueden acceder
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')") // usa 'ADMIN' aquí porque Spring añade ROLE_ internamente
    public String adminAccess() {
        return "Contenido Exclusivo para el ADMINISTRADOR DSP.";
    }
}