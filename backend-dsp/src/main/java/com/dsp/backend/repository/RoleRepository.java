package com.dsp.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dsp.backend.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> { // Asumiendo que Role.id es Integer

    // 1. getRoles (findAll ya hace esto)

    // 2. getNombreRoles
    // Usamos una consulta JPQL (no nativa) para seleccionar solo el campo 'name' del objeto Role.
    @Query("SELECT r.name FROM Role r")
    List<String> findAllRoleNames();
    
    Optional<Role> findByName(String name);
}