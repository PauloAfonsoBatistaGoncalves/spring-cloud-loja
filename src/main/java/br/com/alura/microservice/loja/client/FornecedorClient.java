package br.com.alura.microservice.loja.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.alura.microservice.loja.controller.dto.InfoFornecedorDto;

@FeignClient("fornecedor")
public interface FornecedorClient {
	
	@GetMapping("/info/{estado}")
	public InfoFornecedorDto getInfoPorEstado(@PathVariable String estado);
}
