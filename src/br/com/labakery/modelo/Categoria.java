package br.com.labakery.modelo;

import java.io.Serializable;

public class Categoria implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int idcategoria;
	private String descricao;
	
	public int getIdcategoria() {
		return idcategoria;
	}
	
	public void setIdcategoria(int idcategoria) {
		this.idcategoria = idcategoria;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
