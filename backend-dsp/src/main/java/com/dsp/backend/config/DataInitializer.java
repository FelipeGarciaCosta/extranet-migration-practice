package com.dsp.backend.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dsp.backend.model.Role;
import com.dsp.backend.model.Usuario;
import com.dsp.backend.repository.RoleRepository;
import com.dsp.backend.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    // Inyectamos los Repositorios y el codificador de contraseñas
    // (PasswordEncoder es el BCrypt que configuramos en SecurityConfig)

    @Bean
    public CommandLineRunner initDatabase(
            RoleRepository roleRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        
        return args -> {
            // 1. Crear y Guardar el Rol de Prueba si no existe
            if (roleRepository.findAll().isEmpty()) {
                Role adminRole = new Role();
                adminRole.setName("ROLE_ADMIN"); 
                roleRepository.save(adminRole);

                Role userRole = new Role();
                userRole.setName("ROLE_AGENT");
                roleRepository.save(userRole);
                
                Role agentRole = new Role();
                agentRole.setName("ROLE_USER");
                roleRepository.save(agentRole);
            }

            // 2. Crear y Guardar el Usuario de Prueba si no existe
            if (usuarioRepository.findAll().isEmpty()) {
                
                // Obtener el rol que acabamos de guardar
                Role adminRole = roleRepository.findAllRoleNames().stream()
                                    .filter(name -> name.equals("ROLE_ADMIN"))
                                    .findFirst()
                                    .map(name -> { 
                                        Role r = new Role(); 
                                        r.setName(name); 
                                        return r; 
                                    })
                                    .orElse(null); // Esto es una simplificación, asume que lo encuentra
                
                // Si el rol existe, crea el usuario
                if (adminRole != null) {
                    Usuario admin = new Usuario();
                    admin.setUsername("admin");
                    
                    // La contraseña debe estar cifrada con BCrypt
                    admin.setPassword(passwordEncoder.encode("mipasslarga")); 
                    
                    admin.setNombre("Admin");
                    admin.setApellidos("Principal");
                    admin.setEmail("admin@cia-dsp.com");
                    admin.setActivo(true);
                    
                    Set<Role> roles = new HashSet<>();
                    roles.add(adminRole);
                    admin.setRoles(roles); // Asumiendo que tu entidad Usuario tiene setRoles(Set<Role>)

                    usuarioRepository.save(admin);
                    System.out.println(">>> Usuario Admin de prueba creado.");
                } else {
                     System.err.println(">>> ERROR: No se encontró el rol ROLE_ADMIN para asignar.");
                }
            }
        };
    }
}