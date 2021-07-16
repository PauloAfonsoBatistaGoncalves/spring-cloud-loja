package br.com.alura.microservice.loja.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.com.alura.microservice.loja.client.FornecedorClient;
import br.com.alura.microservice.loja.controller.dto.CompraDto;
import br.com.alura.microservice.loja.controller.dto.InfoFornecedorDto;
import br.com.alura.microservice.loja.controller.dto.InfoPedidoDto;
import br.com.alura.microservice.loja.model.Compra;
import br.com.alura.microservice.loja.repository.CompraRepository;

@Service
public class CompraService {

	private static Logger LOG = LoggerFactory.getLogger(CompraService.class);

	@Autowired
	private FornecedorClient fornecedorClient;

	@Autowired
	private CompraRepository compraRepository;

	@HystrixCommand(fallbackMethod = "realizaCompraFallback",
			threadPoolKey = "realizarCompraThreadPool")
	public Compra realizarCompra(CompraDto compra) {
		final String estado = compra.getEndereco().getEstado();

		LOG.info("Buscando informações do fornecedor de {}", estado);
		InfoFornecedorDto info = fornecedorClient.getInfoPorEstado(estado);

		LOG.info("Realizando um pedido");
		InfoPedidoDto infoPedido = fornecedorClient.realizaPedido(compra.getItens());

		Compra compraSalva = new Compra();
		compraSalva.setPedidoId(infoPedido.getId());
		compraSalva.setTempoDePreparo(infoPedido.getTempoDePreparo());
		compraSalva.setEnderecoDestino(info.getEndereco());

		compraRepository.save(compraSalva);

		return compraSalva;
	}

	@HystrixCommand(threadPoolKey = "getByIdThreadPool")
	public Compra getById(Long id) {
		return compraRepository.findById(id).orElseThrow();
	}

	public Compra realizaCompraFallback(CompraDto compra) {
		Compra compraFallback = new Compra();
		compraFallback.setEnderecoDestino(compra.getEndereco().toString());

		return compraFallback;
	}



}
