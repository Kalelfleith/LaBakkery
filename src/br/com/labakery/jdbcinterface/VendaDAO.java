package br.com.labakery.jdbcinterface;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.labakery.modelo.Usuario;
import br.com.labakery.modelo.Venda;

public interface VendaDAO {
	
	public int adicionar (Venda venda);
	public Usuario buscarIdUsuario(String nome);

}
