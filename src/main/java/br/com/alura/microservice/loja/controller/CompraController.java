package br.com.alura.microservice.loja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.microservice.loja.controller.dto.CompraDto;
import br.com.alura.microservice.loja.model.Compra;
import br.com.alura.microservice.loja.service.CompraService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/compra")
public class CompraController {

	@Autowired
	private CompraService compraService;
	
	@PostMapping
	public Compra realizaCompra(@RequestBody CompraDto compra) throws InterruptedException {
		return compraService.realizarCompra(compra);
	}
	
	@GetMapping("/{id}")
	public Compra getById(@PathVariable Long id) {
		return compraService.getById(id);
	}
}
