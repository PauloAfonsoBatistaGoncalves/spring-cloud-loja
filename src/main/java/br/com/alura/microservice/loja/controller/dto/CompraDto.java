package br.com.alura.microservice.loja.controller.dto;

import java.util.List;

public class CompraDto {

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
	
	
}
