package com.dsp.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.dsp.backend.model.Clientes;

public interface ClienteRepository extends JpaRepository<Clientes, String> {

	@Query("from Clientes c where c.activoCli = true order by c.nombreCli asc")
	List<Clientes> getClientes();
	
	List<Clientes> findByCodigoCliAndActivoCliTrueOrderByNombreCliAsc(@Param("codCli") String codCli);

	//funcion para desactivar un cliente
	@Modifying
	@Transactional
	@Query("update Clientes c set c.activoCli = false where c.codigoCli = :codCli")
	void desactivarCliente(@Param("codCli") String codCli);

	//funcion para activar un cliente
	@Modifying
	@Transactional
	@Query("update Clientes c set c.activoCli = true where c.codigoCli = :codCli")
	void activarCliente(@Param("codCli") String codCli);

	// Obtener cliente por código (equivalente a getCliente)
	@Query("from Clientes c where c.codigoCli = :clienteRec")
	Clientes getClienteByCodcli(@Param("clienteRec") String clienteRec);
	

}