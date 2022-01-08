package br.com.labakery.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import br.com.labakery.jdbcinterface.ProdutoDAO;
import br.com.labakery.modelo.Categoria;
import br.com.labakery.modelo.Produto;


public class JDBCProdutoDAO implements ProdutoDAO {
	
	private Connection conexao;
	
	public JDBCProdutoDAO(Connection conexao) {
		this.conexao = conexao;
	}
	
	public boolean inserir(Produto produto){
	
		String comando = "INSERT INTO produto (idProduto, descricao, preco, quantidade, categoria) VALUES (?,?,?,?,?)";
	
		PreparedStatement p;
	
		try {
		
			p = this.conexao.prepareStatement(comando);
			
			p.setInt(1, produto.getIdProduto());
			p.setString(2, produto.getDescricao());
			p.setFloat(3, produto.getPreco());
			p.setInt(4, produto.getQuantidade());
			p.setString(5, produto.getCategoria());
			
			p.execute();			
		
		
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	

	}
	
	//Criado o método para buscar a quantide de produtos no Estoque / Processo VENDA
	public List<Produto> buscar() {
		
		String comando = "SELECT * FROM produto";
		
		List<Produto> listProduto = new ArrayList<Produto>();
		
		Produto produto = null;
		
		try {
			
			Statement stmt = conexao.createStatement();
			
			ResultSet rs = stmt.executeQuery(comando);
			
			while (rs.next()) {
				
				produto = new Produto();
				
				int idProduto = rs.getInt("idProduto");
				String descricao = rs.getString("descricao");
				float preco = rs.getFloat("preco");
				int quantidade = rs.getInt("quantidade");
				String categoria = rs.getString("categoria");
				
				produto.setIdProduto(idProduto);
				produto.setDescricao(descricao);
				produto.setPreco(preco);
				produto.setQuantidade(quantidade);
				produto.setCategoria(categoria);
				
				listProduto.add(produto);
			}
			
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return listProduto;
	}
	
	public List<JsonObject> buscarPorNome(String nome){
		
		String comando = "SELECT * FROM produto WHERE descricao LIKE '%"+ nome + "%'";
		
		List<JsonObject> listaProdutos = new ArrayList<JsonObject>();
		JsonObject produto = null;

		try {
			
			Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);

			while (rs.next()) {

				int idProduto = rs.getInt("idProduto");
				String descricao = rs.getString("descricao");
				float preco = rs.getFloat("preco");
				int quantidade = rs.getInt("quantidade");
				String categoria = rs.getString("categoria");

				produto = new JsonObject();
				produto.addProperty("idProduto", idProduto);
				produto.addProperty("descricao", descricao);
				produto.addProperty("preco", preco);
				produto.addProperty("quantidade", quantidade);
				produto.addProperty("categoria", categoria);
				
				listaProdutos.add(produto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaProdutos;
		
	}
	
	public boolean deletar (int id) {
		
		String comando = "DELETE FROM produto WHERE idProduto = ? ";
		PreparedStatement p;
		try {
			p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			p.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	public Produto buscarPorId(int id) {
		String comando = "SELECT * FROM produto WHERE idProduto =? ";
		Produto produto = new Produto();
		try {
			PreparedStatement p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {

			int idProduto = rs.getInt("idProduto");
			String descricao = rs.getString("descricao");
			float preco = rs.getFloat("preco");
			int quantidade = rs.getInt("quantidade");
			String categoria = rs.getString("categoria");
			
			produto.setIdProduto(idProduto);
			produto.setDescricao(descricao);
			produto.setPreco(preco);
			produto.setQuantidade(quantidade);
			produto.setCategoria(categoria);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return produto;
	}
	
	public boolean alterar(Produto produto) {

		String comando = "UPDATE produto " + "SET descricao=?, preco=?, quantidade=?, categoria=? "
				+ " WHERE idProduto=? ";
		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando);
			p.setString(1, produto.getDescricao());
			p.setFloat(2, produto.getPreco());
			p.setInt(3, produto.getQuantidade());
			p.setString(4, produto.getCategoria());
			p.setInt(5, produto.getIdProduto());
			p.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Produto buscarProdutoEstoque(int id) {
		String comando = "SELECT * FROM produto WHERE idProduto =? ";
		Produto produto = new Produto();
		try {
			PreparedStatement p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {

			int idProduto = rs.getInt("idProduto");
			String descricao = rs.getString("descricao");
			float preco = rs.getFloat("preco");
			int quantidade = rs.getInt("quantidade");
			String categoria = rs.getString("categoria");
			
			produto.setIdProduto(idProduto);
			produto.setDescricao(descricao);
			produto.setPreco(preco);
			produto.setQuantidade(quantidade);
			produto.setCategoria(categoria);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return produto;
	}
	
}
