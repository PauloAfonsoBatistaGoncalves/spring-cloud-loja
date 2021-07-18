package br.com.alura.microservice.loja.controller.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CompraDto {

	@JsonIgnore
	private Long compraId;
	private List<ItemDaCompraDto> itens;
	private EnderecoDto endereco;
	public List<ItemDaCompraDto> getItens() {
		return itens;
	}
	public void setItens(List<ItemDaCompraDto> itens) {
		this.itens = itens;
	}
	public EnderecoDto getEndereco() {
		return endereco;
	}
	public void setEndereco(EnderecoDto endereco) {
		this.endereco = endereco;
	}
	public Long getCompraId() {
		return compraId;
	}
	public void setCompraId(Long compraId) {
		this.compraId = compraId;
	}
	
	
}
