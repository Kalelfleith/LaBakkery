package br.com.labakery.modelo;

import java.io.Serializable;

public class Produto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int idProduto;
	private String descricao;
	private float preco;
	private int quantidade;
	private String categoria;
	
	public int getIdProduto() {
		return idProduto;
	}
	
	public void setIdProduto(int idProduto) {
		this.idProduto = idProduto;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao (String descricao) {
		this.descricao = descricao;
	}

	public float getPreco() {
		return preco;
	}
	
	public void setPreco (float preco) {
		this.preco = preco;
	}

	public int getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade (int quantidade) {
		this.quantidade = quantidade;
	}
	
	public String getCategoria() {
		return categoria;
	}
	
	public void setCategoria (String categoria) {
		this.categoria = categoria;
	}

}
