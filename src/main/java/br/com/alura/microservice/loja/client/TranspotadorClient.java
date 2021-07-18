package br.com.alura.microservice.loja.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.alura.microservice.loja.controller.dto.InfoEntregaDto;
import br.com.alura.microservice.loja.controller.dto.VoucherDto;

@FeignClient("transportador")
public interface TranspotadorClient {

	@PostMapping("/entrega")
	VoucherDto reservaEntrega(InfoEntregaDto entregaDto);

}
