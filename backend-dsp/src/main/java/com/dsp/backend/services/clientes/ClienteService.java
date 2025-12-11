package com.dsp.backend.services.clientes;

import java.util.List;

import com.dsp.backend.model.Clientes;


public interface ClienteService {

	public List<Clientes> getClientes();
	
	public List<Clientes> getLsClientesByCodigo(String codcli);

	public Clientes getClienteByCodcli(String codcli);
		
	public boolean activarCliente(String codCli);
	
	public boolean desactivarCliente(String codCli);
}