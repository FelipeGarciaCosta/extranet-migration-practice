package com.dsp.backend.model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="clientes")
@XmlRootElement(name="cliente")
@XmlAccessorType(XmlAccessType.FIELD)
public class Clientes  implements java.io.Serializable {

    
	private static final long serialVersionUID = 1L;
	
     @Id     
	 @Column(name="codigoCli", unique=true, nullable=false, length=12)
	 private String codigoCli;
     @Column(name="nombreCli", nullable=false, length=80)
     private String nombreCli;
     @Column(name="agenteCli", length=12)
     private String agenteCli;
     @Column(name="activoCli", length=1)
     private boolean activoCli;
     @Column(name="formenvCli", length=2)
     private String formenvCli;
     @Column(name="formpagCli", length=2)
     private String formpagCli;
     @Column(name="cifCli", length=15)
     private String cifCli;
     @Column(name="titularCli", length=80)
     private String titularCli;
     private Integer enlaceCli;
     private String cliente2Cli;
     
	
	
//	@Transient  /***NO funciona esto**/
//    @XmlElementWrapper(name="lsDireccDTO")
//    @XmlElement(name="direccDTO")
//    @ElementCollection(targetClass=DireccDTO.class)
//	public List<DireccDTO> getLsDirecciones() {
//		return lsDirecciones;
//	}
//
//	public void setLsDirecciones(List<DireccDTO> lsDirecciones) {
//		this.lsDirecciones = lsDirecciones;
//	}

	@Override
	public int hashCode() {
		return Integer.valueOf(codigoCli);
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Clientes) {
			Clientes cliente = (Clientes) obj;
			return cliente.getCodigoCli() == codigoCli;
		}
		return false;
	}

	@Override
	public String toString() {
	    StringBuffer strBuff = new StringBuffer();
	    strBuff.append(getNombreCli());
	    return strBuff.toString();
	}

}


