package com.dsp.backend.services.clientes.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dsp.backend.model.Clientes;
import com.dsp.backend.repository.ClienteRepository;
import com.dsp.backend.services.clientes.ClienteService;

@Service("ClienteService")
@Transactional
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public List<Clientes> getClientes() {
		
		return clienteRepository.getClientes();
	}

	@Override
	public List<Clientes> getLsClientesByCodigo(String codcli) {
		
		return clienteRepository.findByCodigoCliAndActivoCliTrueOrderByNombreCliAsc(codcli);
	}

	@Override
	public Clientes getClienteByCodcli(String codcli) {
		
		return clienteRepository.getClienteByCodcli(codcli);
	}

	@Override
	public boolean activarCliente(String codCli) {
		try {
			clienteRepository.activarCliente(codCli);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean desactivarCliente(String codCli) {
		try {
			clienteRepository.desactivarCliente(codCli);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
