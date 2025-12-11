package com.dsp.backend.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuario")
@ToString(exclude = "roles") // evitar imprimir roles en toString (previene problemas de log infinito)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true, nullable = false)
    private String username;

    private String password;
    private String nombre;
    private String apellidos;

    @Column(name = "isActivo")
    private boolean activo;

    @Column(unique = true)
    private String email;

    private boolean pagado;

    @Transient
    private String strPagado;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_roles",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> roles = new HashSet<>();

    // --- Helpers para mantener la relación bidireccional correctamente
    public void addRole(Role role) {
        if (role == null) return;
        this.roles.add(role);
        if (role.getUsuarios() == null) {
            role.setUsuarios(new HashSet<>());
        }
        role.getUsuarios().add(this);
    }

    public void removeRole(Role role) {
        if (role == null) return;
        this.roles.remove(role);
        if (role.getUsuarios() != null) {
            role.getUsuarios().remove(this);
        }
    }
}
