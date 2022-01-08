package br.com.labakery.rest;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.labakery.bd.Conexao;
import br.com.labakery.jdbc.JDBCCategoriaDAO;
import br.com.labakery.jdbc.JDBCProdutoDAO;
import br.com.labakery.jdbc.JDBCUsuarioDAO;
import br.com.labakery.modelo.Categoria;
import br.com.labakery.modelo.Produto;
import br.com.labakery.modelo.Usuario;
import br.com.labakery.modelo.ValidaCadastro;

@Path("usuario")
public class UsuarioRest extends UtilRest {
	
	@POST
	@Path("/verificaUsuario")
	@Consumes("application/*")
	public Response verificaUsuario(String login) {
		
		try {
			
			ValidaCadastro dadoscadastro = new Gson().fromJson(login, ValidaCadastro.class);

			Conexao conec = new Conexao();
			Connection conexao = (Connection)conec.abrirConexao();
	
			JDBCUsuarioDAO jdbcAutentica = new JDBCUsuarioDAO(conexao);
			int verifica = jdbcAutentica.verificaUsuario(dadoscadastro);
			
			return this.buildResponse(verifica);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	
	};
	
	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir(String usuarioParam) {
		
		try {
			Usuario usuario = new Gson().fromJson(usuarioParam, Usuario.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			
			//Implementação da Conversão de senhabse64 para MD5
			String senmd5 = "";
			MessageDigest md = null;
			
			try {
				
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				
				e.printStackTrace();
				
			}
			
			BigInteger hash = new BigInteger(1, md.digest(usuario.getSenha().getBytes()));
			System.out.println(usuario.getSenha());
			
			senmd5 = hash.toString(16);
			
			usuario.setSenha(senmd5);
			
			JDBCUsuarioDAO jdbcUsuario = new JDBCUsuarioDAO(conexao);
			boolean retorno = jdbcUsuario.inserir(usuario);
			String msg = "";
			
			if(retorno) {
				msg = "Usuario cadastrado com sucesso!";
			}else {
				msg = "Erro ao cadastrar Usuario.";
			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
		
	}
	
	@GET
	@Path("/buscar")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorNome(@QueryParam("valorBusca") String nome) {
		
		
		try {
			
			List<JsonObject> listaUsuario = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCUsuarioDAO jdbcUsuario = new JDBCUsuarioDAO(conexao);
			listaUsuario = jdbcUsuario.buscarPorNome(nome);
			conec.fecharConexao();
			
						
			String json = new Gson().toJson(listaUsuario);
			return this.buildResponse(json);
			
				
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@DELETE
	@Path("/excluir/{id}")
	@Consumes("application/*")
	public Response excluir(@PathParam("id") int id) {
		
		
		try {
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCUsuarioDAO jdbcUsuario = new JDBCUsuarioDAO(conexao);
			
			boolean retorno = jdbcUsuario.deletar(id);
			
			String msg = "";
			if(retorno) {
				msg = "Usuário excluído com sucesso!";
			}else {
				msg = "Erro ao excluir Usuário.";
			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}
	
	@GET
	@Path("/buscarPorId")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorId(@QueryParam("id") int id) {
		
		try {
			Usuario usuario = new Usuario();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCUsuarioDAO jdbcUsuario = new JDBCUsuarioDAO(conexao);
			
			usuario = jdbcUsuario.buscarPorId(id);
			
			conec.fecharConexao();
			
			return this.buildResponse(usuario);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@PUT
	@Path("/alterar")
	@Consumes("application/*")
	public Response alterar(String usuarioParam) {
		
		
		try {
			Usuario usuario = new Gson().fromJson(usuarioParam, Usuario.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCUsuarioDAO jdbcUsuario = new JDBCUsuarioDAO(conexao);
			
			boolean retorno = jdbcUsuario.alterar(usuario);
			
			String msg = "";
			if (retorno) {
				msg = "Usuário alterado com sucesso!";
			}else {
				msg = "Erro ao alterar Usuário.";
			}
			
			conec.fecharConexao();
			return this.buildResponse(msg);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}

}
