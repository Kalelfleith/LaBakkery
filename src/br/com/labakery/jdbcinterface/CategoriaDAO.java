package br.com.labakery.jdbcinterface;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.labakery.modelo.Categoria;

public interface CategoriaDAO {
	
	public List<Categoria> buscar();
	public boolean inserirCategoria(Categoria categoria);
	public List<JsonObject> buscarPorNome (String nome);
	public boolean deletar (int id);
	public Categoria buscarPorId(int id);
	public boolean alterar(Categoria categoria);

}
