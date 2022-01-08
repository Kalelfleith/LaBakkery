package br.com.labakery.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import br.com.labakery.jdbcinterface.FaturamentoDAO;
import br.com.labakery.modelo.ConsultaData;
import br.com.labakery.modelo.Venda;

public class JDBCFaturamentoDAO implements FaturamentoDAO {
	
	private Connection conexao;
	
	public JDBCFaturamentoDAO(Connection conexao) {
		this.conexao = conexao;
	}
	
	public List<JsonObject> buscarProdutoMaisVendido() {
		
		String comando = "SELECT produto_idProduto, produto.descricao, sum(produto_quantidade) AS qtd_total, SUM(produto_quantidade)*produto.preco AS valorTotal FROM produto_has_venda "
				+"JOIN produto ON produto_idProduto = idproduto GROUP BY produto_idProduto, produto.descricao ORDER BY qtd_total DESC";
		
		List<JsonObject> listProduto = new ArrayList<JsonObject>();
		
		try {
			
			Statement stmt = conexao.createStatement();
			
			ResultSet rs = stmt.executeQuery(comando);
			
			while (rs.next()) {
				
				int idProduto = rs.getInt("produto_idProduto");
				String descricao = rs.getString("produto.descricao");
				int qtdTotal = rs.getInt("qtd_total");
				int valorTotal = rs.getInt("valorTotal");
				
				JsonObject resultadoConsulta = new JsonObject();
				
				resultadoConsulta.addProperty("produto_idProduto", idProduto);
				resultadoConsulta.addProperty("descricao", descricao);
				resultadoConsulta.addProperty("qtdTotal", qtdTotal);
				resultadoConsulta.addProperty("valorTotal", valorTotal);
				
				listProduto.add(resultadoConsulta);
				
			}
			
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return listProduto;
	}
	
	public List<JsonObject> consultaVendaPorData(ConsultaData valorData){
		
		String comando = "SELECT produto_idProduto, produto.descricao, venda.data_venda, sum(produto_quantidade) AS qtd_total, SUM(produto_quantidade)*produto.preco AS valorTotal FROM produto_has_venda " + 
				"JOIN produto ON produto_idProduto = idproduto "  + 
				"JOIN venda ON venda_idvenda = idvenda WHERE data_venda >= '"+valorData.getData1()+"' AND data_venda <= '"+valorData.getData2()+"' GROUP BY produto_idProduto, produto.descricao, venda.data_venda ORDER BY qtd_total DESC";
	
		List<JsonObject> listProduto = new ArrayList<JsonObject>();
	
		try {
			
			Statement stmt = conexao.createStatement();
			
			ResultSet rs = stmt.executeQuery(comando);
			
			while(rs.next()) {
				
				int idProduto = rs.getInt("produto_idProduto");
				String descricao = rs.getString("produto.descricao");
				String data = rs.getString("venda.data_venda");
				int qtdTotal = rs.getInt("qtd_total");
				int valorTotal = rs.getInt("valorTotal");
				
				JsonObject resultadoConsulta = new JsonObject();
				
				resultadoConsulta.addProperty("produto_idProduto", idProduto);
				resultadoConsulta.addProperty("descricao", descricao);
				resultadoConsulta.addProperty("dataVenda", data);
				resultadoConsulta.addProperty("qtdTotal", qtdTotal);
				resultadoConsulta.addProperty("valorTotal", valorTotal);
				
				listProduto.add(resultadoConsulta);
			}
			
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listProduto;

	}

}
