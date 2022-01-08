package br.com.labakery.modelo;

import java.io.Serializable;

public class ProdutoHasVenda implements Serializable {
	
	private static final long serivalVersionUID = 1L;
	
	private int idproduto_has_venda;
	private int venda_idvenda;
	private int produto_idProduto;
	private int produto_quantidade;
	
	public int getIdproduto_has_venda() {
		return idproduto_has_venda;
	}
	
	public void setIdproduto_has_venda(int idproduto_has_venda) {
		this.idproduto_has_venda = idproduto_has_venda;
	}
	
	public int getVenda_idvenda() {
		return venda_idvenda;
	}
	
	public void setVenda_idvenda(int venda_idvenda) {
		this.venda_idvenda = venda_idvenda;
	}
	
	public int getProduto_idProduto() {
		return produto_idProduto;
	}
	
	public void setProduto_idProduto(int produto_idProduto) {
		this.produto_idProduto = produto_idProduto;
	}
	
	public int getProduto_quantidade() {
		return produto_quantidade;
	}
	
	public void setProduto_quantidade(int produto_quantidade) {
		this.produto_quantidade = produto_quantidade;
	}

}
