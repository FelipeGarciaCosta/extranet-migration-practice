package com.dsp.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name="Empresa")
public class Empresa {
	@Id
    @Column(name="IdEmpresa", unique = true, nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int idEmpresa;
	@Column(name="nombre")
    private String nombre;
    private boolean detalles;
    private boolean dtoLinea;
	@Column(name="factor", nullable=true)
    private Boolean factor;
    
	@Override
	public int hashCode() {
		return idEmpresa;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Empresa) {
			Empresa empresa = (Empresa) obj;
			return empresa.getIdEmpresa() == idEmpresa;
		}
		return false;
	}

	@Override
    public String toString() {
        StringBuffer strBuff = new StringBuffer();
        strBuff.append("Empresa: ").append(getNombre());
        return strBuff.toString();
    }

}