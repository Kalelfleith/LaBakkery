package br.com.labakery.jdbcinterface;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.labakery.modelo.Usuario;
import br.com.labakery.modelo.ValidaCadastro;

public interface UsuarioDAO {
	
	public boolean inserir (Usuario usuario);
	public int verificaUsuario (ValidaCadastro dadoscadastro);
	public List<JsonObject> buscarPorNome (String nome);
	public boolean deletar (int id);
	public Usuario buscarPorId(int id);
	public boolean alterar (Usuario usuario);

}
