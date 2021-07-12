package br.com.alura.microservice.loja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.microservice.loja.client.FornecedorClient;
import br.com.alura.microservice.loja.controller.dto.CompraDto;
import br.com.alura.microservice.loja.controller.dto.InfoFornecedorDto;

@Service
public class CompraService {
	
	@Autowired
	private FornecedorClient fornecedorClient;
	
	public void realizarCompra(CompraDto compra) {
		InfoFornecedorDto info = fornecedorClient.getInfoPorEstado(compra.getEndereco().getEstado());
		System.out.println(info.getEndereco());
	}

}
