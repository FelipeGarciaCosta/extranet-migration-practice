package com.dsp.backend.model;




import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class DireccId  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
    @Column(name="clienteDir", nullable=false, length=12)
	private String clienteDir;
    @Column(name="numeroDir", nullable=false)
    private Integer numeroDir;


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof DireccId) ) return false;
		 DireccId castOther = ( DireccId ) other; 
         
		 return ( (this.getClienteDir()==castOther.getClienteDir()) || ( this.getClienteDir()!=null && castOther.getClienteDir()!=null && this.getClienteDir().equals(castOther.getClienteDir()) ) )
 && ( (this.getNumeroDir()==castOther.getNumeroDir()) || ( this.getNumeroDir()!=null && castOther.getNumeroDir()!=null && this.getNumeroDir().equals(castOther.getNumeroDir()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getClienteDir() == null ? 0 : this.getClienteDir().hashCode() );
         result = 37 * result + ( getNumeroDir() == null ? 0 : this.getNumeroDir().hashCode() );
         return result;
   }   
}


