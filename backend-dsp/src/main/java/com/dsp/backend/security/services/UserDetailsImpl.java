package com.dsp.backend.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dsp.backend.model.Role;
import com.dsp.backend.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    
    // @JsonIgnore para evitar enviar la contraseña al frontend
    @JsonIgnore
    private String password; 
    
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled; 

    public UserDetailsImpl(Long id, String username, String password,
                           Collection<? extends GrantedAuthority> authorities,
                           boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    // Método estático para construir este objeto a partir de tu Entidad de Usuario
    public static UserDetailsImpl build(Usuario usuario) {
        List<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(Role::getName) // asume que role.getName() devuelve por ejemplo "ADMIN" o "ROLE_ADMIN"
                .map(roleName -> {
                    // Normaliza: si tu DB almacena "ADMIN" añadimos el prefijo ROLE_, si ya está con ROLE_ lo dejamos
                    if (roleName.startsWith("ROLE_")) {
                        return new SimpleGrantedAuthority(roleName);
                    } else {
                        return new SimpleGrantedAuthority("ROLE_" + roleName);
                    }
                })
                .collect(Collectors.toList());
        	boolean enabled = usuario.isActivo();

            return new UserDetailsImpl(usuario.getId(), usuario.getUsername(), usuario.getPassword(), authorities, enabled);
        }
    // --- Implementación de UserDetails (Métodos requeridos) ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
    
    // Implementa aquí la lógica de tu entidad (si el usuario está activo, no expirado, etc.)
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return enabled; }

    // Asegura que dos UserDetailsImpl sean iguales si tienen el mismo ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}