package br.com.labakery.jdbcinterface;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.labakery.modelo.Produto;

public interface ProdutoDAO {
	
	public boolean inserir (Produto produto);
	public List<JsonObject> buscarPorNome (String nome);
	public boolean deletar (int id);
	public Produto buscarPorId(int id);
	public boolean alterar(Produto produto);
	public Produto buscarProdutoEstoque(int id);

}
