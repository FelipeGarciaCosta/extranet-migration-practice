package com.dsp.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dsp.backend.model.Usuario;

// JpaRepository implementa automáticamente: save, saveAll, findById, findAll, delete, etc.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // 1. findByUserName / getUser(String login)
    // Usamos 'username' para el campo estándar en Spring Security
    // Spring Data JPA lo implementa automáticamente por el nombre del método
    Optional<Usuario> findByUsername(String username); 
    
    // Si todavía necesitas buscar por el campo 'login' (que ahora se llama username en Java pero mapea a 'login' en la DB):
    // Optional<Usuario> findByLogin(String login); // Si tu campo Java se llama login

    // 2. getUsuariosActivos
    // Implementación automática: Busca por el campo 'isActivo' en la entidad
    List<Usuario> findByActivoTrue();
    
    // 3. getRoles / getNombreRoles
    // Para esto, necesitarás un RoleRepository (ver abajo).

    // 4. getIdsUsuariosActivosInt (Ejemplo de consulta nativa si fuera necesaria, pero evítala si puedes)
    // Si el findByIsActivoTrue() no funciona por algún mapeo de nombres de columna, usarías:
    // @Query(value = "SELECT u.id FROM user u WHERE u.isActivo = 1", nativeQuery = true)
    // List<Integer> findIdsOfActiveUsers();
    
    // Método que garantiza traer roles junto con el usuario (IMPORTANTE)
    @Query("select u from Usuario u left join fetch u.roles where u.username = :username")
    Optional<Usuario> findByUsernameFetchRoles(@Param("username") String username);
}