package com.dsp.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsp.backend.model.Clientes;
import com.dsp.backend.services.clientes.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClientesController {

	private final ClienteService clienteService;
	

	public ClientesController(ClienteService clienteService) {
		this.clienteService = clienteService;
		
	}
	
	@GetMapping
	public ResponseEntity<List<Clientes>> getClientes() {
		
		List<Clientes> lsClientes = clienteService.getClientes();
		return ResponseEntity.ok(lsClientes);
	}
		
	@GetMapping("/{codCli}")
	public ResponseEntity<Clientes> getClientesByCodigo(@PathVariable String codCli) {
		
		Clientes cliente = clienteService.getClienteByCodcli(codCli);
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}
	
	@PatchMapping("/activar/{codCli}")
	public ResponseEntity<Void> activarCliente(@PathVariable String codCli) {
				
		boolean activado = clienteService.activarCliente(codCli);
        if (activado) {
            // Éxito en la modificación, pero sin cuerpo de respuesta.
            return ResponseEntity.noContent().build(); // 204 NO CONTENT 
        } else {
            // El cliente no existe para ser activado.
            return ResponseEntity.notFound().build(); // 404 NOT FOUND
        }
	}
	
	@PatchMapping("/desactivar/{codCli}")
	public ResponseEntity<Void> desactivarCliente(@PathVariable String codCli) {
			
		clienteService.desactivarCliente(codCli);
		return ResponseEntity.noContent().build();
	}

}