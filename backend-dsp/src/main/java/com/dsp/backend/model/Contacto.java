package com.dsp.backend.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="contacto")
public class Contacto implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id     
	@Column(name="codigoCon", unique=true, nullable=false)
	private Integer codigoCon;
	
	@Column(name="clienteCon")
	private String cliente;
	
	@Column(name="codcli")
	private String codcli;
	
	@Column(name="nombreCon", length=80)
	private String nombreCon;
	
	@Column(name="telefonoCon", length=20)
	private String telefonoCon;
	
	@Column(name="movilCon", length=20)
	private String movilCon;
	
	@Column(name="emailCon", length=80)
	private String emailCon;
			
	@Column(name="observCon")
	private String observCon;
							
	@Column(name="activoCon")
	private boolean activoCon;
	
	@Column(name="principalCon")
	private boolean principalCon;
	
	@Column(name="usuarioCon", length=80)
	private String usuarioCon;
	
	private String agenteCon;
	
	@Column(name="cifCon", length=12)
	private String cifCon;
	private Date fechaMod;
	private Integer idContactoERP;
	
	//@OneToOne(mappedBy = "contactoDcn", cascade = CascadeType.ALL, 
    //        fetch = FetchType.EAGER, optional = false)
	//private Direcc direc;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoCon == null) ? 0 : codigoCon.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Contacto) {
			Contacto contacto = (Contacto) obj;
			return contacto.getCodigoCon() == codigoCon;
		}
		return false;
	}

	@Override
    public String toString() {
        StringBuffer strBuff = new StringBuffer();
        strBuff.append("Contacto: ").append(getNombreCon());
        return strBuff.toString();
    }
}
