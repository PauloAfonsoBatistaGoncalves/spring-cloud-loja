package br.com.alura.microservice.loja.service;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.alura.microservice.loja.controller.dto.CompraDto;
import br.com.alura.microservice.loja.controller.dto.InfoFornecedorDto;

public class CompraService {

	public void realizarCompra(CompraDto compra) {
		RestTemplate client = new RestTemplate();
		ResponseEntity<InfoFornecedorDto> exchange = client.exchange("http://localhost:8081/info/" + compra.getEndereco().getEstado(),
				HttpMethod.GET, null, InfoFornecedorDto.class);
	
		System.out.println(exchange.getBody().getEndereco());
	}

}
