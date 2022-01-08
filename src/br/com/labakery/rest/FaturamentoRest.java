package br.com.labakery.rest;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.labakery.bd.Conexao;
import br.com.labakery.jdbc.JDBCFaturamentoDAO;
import br.com.labakery.jdbc.JDBCProdutoDAO;
import br.com.labakery.modelo.ConsultaData;

@Path("faturamento")
public class FaturamentoRest extends UtilRest{
	
	@GET
	@Path("/produtoMaisVendido")
	@Produces(MediaType.APPLICATION_JSON)
	public Response produtoMaisVendido() {
		
		try{
			List<JsonObject> listaProdutos = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCFaturamentoDAO jdbcFaturamento = new JDBCFaturamentoDAO(conexao);
			
			listaProdutos = jdbcFaturamento.buscarProdutoMaisVendido();
			
			conec.fecharConexao();
			return this.buildResponse(listaProdutos);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}
	
	@POST
	@Path("/porPeriodo")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response porPeriodo(String valorParam) {
		
		try {
			
			List<JsonObject> retorno = new ArrayList<JsonObject>();
			
			ConsultaData valorData = new Gson().fromJson(valorParam, ConsultaData.class);
			
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCFaturamentoDAO jdbcFaturamento = new JDBCFaturamentoDAO(conexao);
			retorno = jdbcFaturamento.consultaVendaPorData(valorData);
			
			conec.fecharConexao();
			
			return this.buildResponse(retorno);
			
				
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}

}
