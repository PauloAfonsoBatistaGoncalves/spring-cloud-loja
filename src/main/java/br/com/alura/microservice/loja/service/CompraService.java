package br.com.alura.microservice.loja.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import br.com.alura.microservice.loja.client.FornecedorClient;
import br.com.alura.microservice.loja.client.TranspotadorClient;
import br.com.alura.microservice.loja.controller.dto.CompraDto;
import br.com.alura.microservice.loja.controller.dto.InfoEntregaDto;
import br.com.alura.microservice.loja.controller.dto.InfoFornecedorDto;
import br.com.alura.microservice.loja.controller.dto.InfoPedidoDto;
import br.com.alura.microservice.loja.controller.dto.VoucherDto;
import br.com.alura.microservice.loja.model.Compra;
import br.com.alura.microservice.loja.model.enums.CompraState;
import br.com.alura.microservice.loja.repository.CompraRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.bulkhead.annotation.Bulkhead.Type;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Mono;

@Service
public class CompraService {

	private static final String BACKEND_A = "backendA";
	private static Logger LOG = LoggerFactory.getLogger(CompraService.class);

	@Autowired
	private FornecedorClient fornecedorClient;

	@Autowired
	private CompraRepository compraRepository;

	@Autowired
	private TranspotadorClient transpotadorClient;

	@CircuitBreaker(name = BACKEND_A, fallbackMethod = "realizaCompraFallback")
//	@Bulkhead(name = BACKEND_A , type = Type.THREADPOOL)
//	@TimeLimiter(name = BACKEND_A, fallbackMethod = "realizaCompraFallback")
	public Compra realizarCompra(CompraDto compra)  {
		
		Compra compraSalva = new Compra();
		compraSalva.setEnderecoDestino(compra.getEndereco().toString());
		compraSalva.setState(CompraState.RECEBIDO);
		compraRepository.save(compraSalva);
		compra.setCompraId(compraSalva.getId());
		
		final String estado = compra.getEndereco().getEstado();

		LOG.info("Buscando informações do fornecedor de {}", estado);
		InfoFornecedorDto info = fornecedorClient.getInfoPorEstado(estado);

		LOG.info("Realizando um pedido");
		InfoPedidoDto pedido = fornecedorClient.realizaPedido(compra.getItens());
		compraSalva.setPedidoId(pedido.getId());
		compraSalva.setTempoDePreparo(pedido.getTempoDePreparo());
		compraSalva.setState(CompraState.PEDIDO_REALIZADO);
		compraRepository.save(compraSalva);
		compra.setCompraId(compraSalva.getId());
		
		InfoEntregaDto entregaDto = new InfoEntregaDto();
		entregaDto.setPedidoId(pedido.getId());
		entregaDto.setEnderecoOrigem(info.getEndereco());
		entregaDto.setEnderecoDestino(compra.getEndereco().toString());
		entregaDto.setDataParaEntrega(LocalDate.now().plusDays(pedido.getTempoDePreparo()));
		
		VoucherDto voucher = transpotadorClient.reservaEntrega(entregaDto);
		compraSalva.setState(CompraState.RESERVA_ENTREGA_REALIZADA);
		compraSalva.setDataParaEntrega(voucher.getPrevisaoParaEntrega());
		compraSalva.setVoucher(voucher.getNumero());
		compraRepository.save(compraSalva);
		compra.setCompraId(compraSalva.getId());
		
		return compraSalva;
	}

	public Compra getById(Long id) {
		return compraRepository.findById(id).orElseThrow();
	}

	public Compra realizaCompraFallback(CompraDto compra, Exception e) {
		System.out.println("entrou fallback");
		if(compra.getCompraId() != null) {
			Compra compraSalva = 
					compraRepository.findById(compra.getCompraId()).orElseThrow();
		
			return compraSalva;
		}
		
		Compra compraFallback = new Compra();
		compraFallback.setEnderecoDestino(compra.getEndereco().toString());

		return compraFallback;
	}



}
