package br.com.labakery.rest;

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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.labakery.bd.Conexao;
import br.com.labakery.jdbc.JDBCUsuarioDAO;
import br.com.labakery.jdbc.JDBCVendaDAO;
import br.com.labakery.modelo.ProdutoHasVenda;
import br.com.labakery.modelo.Usuario;
import br.com.labakery.modelo.Venda;

@Path("venda")
public class VendaRest extends UtilRest {
	
	@POST
	@Path("/adicionar")
	@Consumes("application/*")
	public Response adicionar(String vendaParam) {

		try {
			Venda venda = new Gson().fromJson(vendaParam, Venda.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			
			JDBCVendaDAO jdbcVenda = new JDBCVendaDAO(conexao);
			int retornoIdGerado = jdbcVenda.adicionar(venda);
			
			conec.fecharConexao();
			
			return this.buildResponse(retornoIdGerado);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
		
	}
	
	@GET
	@Path("/buscarIdUsuario")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarIdUsuario(@QueryParam("valorBusca") String nome) {
		
		try {
			
			Usuario usuario = new Usuario();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCVendaDAO jdbcVenda = new JDBCVendaDAO(conexao);
			
			usuario = jdbcVenda.buscarIdUsuario(nome);
			
			conec.fecharConexao();
			
			return this.buildResponse(usuario);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/adicionarProduto")
	@Consumes("application/*")
	public Response adicionarProduto(String vendaProdutoParam) {

		try {
			ProdutoHasVenda vendaProduto = new Gson().fromJson(vendaProdutoParam, ProdutoHasVenda.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			
			JDBCVendaDAO jdbcVenda = new JDBCVendaDAO(conexao);
			boolean retorno = jdbcVenda.adicionarProduto(vendaProduto);
			
			String msg = "";
			
			if(retorno) {
				msg = "Produto Inserido na Venda com Sucesso!";
			}else {
				msg = "Erro ao Inserir Produto.";
			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
		
	}
	
	@GET
	@Path("/buscarProdutoVenda")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarProdutoVenda(@QueryParam("valorBusca") int valorBusca) {
		
		try {
			
			List<JsonObject> listaProdutoVenda = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCVendaDAO jdbcVenda = new JDBCVendaDAO(conexao);
			listaProdutoVenda = jdbcVenda.buscarProdutoVenda(valorBusca);
			conec.fecharConexao();
			
			//System.out.println(listaProdutoVenda);
			
						
			String json = new Gson().toJson(listaProdutoVenda);
			
			//System.out.println("JSON:"+json);
			
			return this.buildResponse(json);
			
				
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}
	
	@DELETE
	@Path("/excluirIdProdutoHasVenda/{id}")
	@Consumes("application/*")
	public Response excluirIdProdutoHasVenda(@PathParam("id") int id) {
		
		try {
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCVendaDAO jdbcVenda = new JDBCVendaDAO(conexao);
			
			boolean retorno = jdbcVenda.deletarIdVenda(id);
			
			String msg = "";
			if(retorno) {
				msg = "Item Excluído com Sucesso!";
			}else {
				msg = "Erro ao Excluir Item.";
			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}

}
