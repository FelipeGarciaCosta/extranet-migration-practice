package com.dsp.backend.model;




import org.springframework.data.annotation.Transient;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="direcc")
@XmlRootElement(name="direcc")
@XmlAccessorType(XmlAccessType.FIELD)
public class Direcc  implements java.io.Serializable {

	private static final long serialVersionUID = 4495321449510262070L;
	
    @EmbeddedId
    private DireccId id;
     @Column(name="descripDir", length=15)
     private String descripDir;
     @Column(name="nombreDir", length=40)
     private String nombreDir;
     @Column(name="domicilioDir", length=80)
     private String domicilioDir;
     @Column(name="codposDir", length=8)
     private String codposDir;
     @Column(name="poblacionDir", length=40)
     private String poblacionDir;
     @Column(name="provinciaDir", length=30)
     private String provinciaDir;
     @Column(name="telefonoDir", length=1)
     private String telefonoDir;
     @Column(name="principalDir", length=1)
     private boolean principalDir;
    @Column(name="entregaDir", length=1)
     private boolean entregaDir;

     //----------habías dicho que hasta acá pero lo dejo igual por las dudas ---------------
     @Column(name="formenvDir", length=2)
     private String formenvDir;
    // private String email;
     @XmlTransient
     private Clientes clientes;
    // private User usuario;
     @Column(name="activoDir", length=1)
     private boolean activoDir;
     @Transient
     private String strProvincia;
     
/* 

*/
    
//	@ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name="userDir", insertable=false, updatable=false)
//	public User getUsuario() {
//		return usuario;
//	}
//
//	public void setUsuario(User usuario) {
//		this.usuario = usuario;
//	}

    @Override
    public String toString(){
        return descripDir+" - "+domicilioDir+" - "+codposDir+" - "+poblacionDir+" - "+strProvincia;
    }

    public String dameDirec1(){
        return domicilioDir+" - "+codposDir+" - ";
    }	
    public String dameDirec2(){
        return poblacionDir+" - "+strProvincia;
    }	
}


