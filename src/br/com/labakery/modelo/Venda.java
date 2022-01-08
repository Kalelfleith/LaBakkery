package br.com.labakery.modelo;

import java.io.Serializable;

public class Venda implements Serializable {
	
	private static final long serivalVersionUID = 1L;
	
	private int idvenda;
	private String data_venda;
	private int usuario_idUsuario;
	
	public int getIdvenda() {
		return idvenda;
	}
	
	public void setIdvenda(int idvenda) {
		this.idvenda = idvenda;
	}
	
	public String getDataVenda() {
		return data_venda;
	}
	
	public void setDataVenda(String data_venda) {
		this.data_venda = data_venda;
	}
	
	public int getUsuario () {
		return usuario_idUsuario;
	}
	
	public void setUsuario (int usuario_idUsuario) {
		this.usuario_idUsuario = usuario_idUsuario;
	}
	

}
