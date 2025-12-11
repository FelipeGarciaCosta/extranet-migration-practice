package com.dsp.backend.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "role")
@ToString(exclude = "usuarios")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<Usuario> usuarios = new HashSet<>();

    public void addUsuario(Usuario usuario) {
        if (usuario == null) return;
        this.usuarios.add(usuario);
        if (usuario.getRoles() == null) usuario.setRoles(new HashSet<>());
        usuario.getRoles().add(this);
    }

    public void removeUsuario(Usuario usuario) {
        if (usuario == null) return;
        if (this.usuarios != null) this.usuarios.remove(usuario);
        if (usuario.getRoles() != null) usuario.getRoles().remove(this);
    }
}
