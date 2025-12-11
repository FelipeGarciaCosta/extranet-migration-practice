package com.dsp.backend.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dsp.backend.model.Usuario;
import com.dsp.backend.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepository; // Tu Repositorio (DAO) de usuarios

    /**
     * Spring Security llama a este método al autenticar para cargar al usuario.
     * @param username El nombre de usuario que se está intentando loguear.
     * @return UserDetailsImpl, la representación del usuario que Spring Security usará.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca al usuario en tu base de datos
        Usuario usuario = usuarioRepository.findByUsernameFetchRoles(username) // Asume un método 'findByUsername' en tu Repository
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el nombre: " + username));

        // Construye el objeto UserDetailsImpl
        return UserDetailsImpl.build(usuario);
    }
}