package br.com.labakery.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import br.com.labakery.jdbcinterface.CategoriaDAO;
import br.com.labakery.modelo.Categoria;
import br.com.labakery.modelo.Produto;

public class JDBCCategoriaDAO implements CategoriaDAO {
	
	private Connection conexao;
	
	public JDBCCategoriaDAO(Connection conexao) {
		this.conexao = conexao;
	}
	
	public List<Categoria> buscar() {
		
		String comando = "SELECT * FROM categoria";
		
		List<Categoria> listCategoria = new ArrayList<Categoria>();
		
		Categoria categoria = null;
		
		try {
			
			Statement stmt = conexao.createStatement();
			
			ResultSet rs = stmt.executeQuery(comando);
			
			while (rs.next()) {
				
				categoria = new Categoria();
				
				int idcategoria = rs.getInt("idcategoria");
				String descricao = rs.getString("descricao");
				
				categoria.setIdcategoria(idcategoria);
				categoria.setDescricao(descricao);
				
				listCategoria.add(categoria);
			}
			
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return listCategoria;
	}
	
	public boolean inserirCategoria(Categoria categoria) {
		
		String comando = "INSERT INTO categoria (idcategoria, descricao) VALUES (?,?)";
		
		PreparedStatement p;
		
		try {
			
			p = this.conexao.prepareStatement(comando);
			
			p.setInt(1, categoria.getIdcategoria());
			p.setString(2, categoria.getDescricao());
						
			p.execute();			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	public List<JsonObject> buscarPorNome(String nome) {
		
		String comando = "SELECT * FROM categoria WHERE descricao LIKE '%"+ nome + "%'";
		
		List<JsonObject> listaCategoria = new ArrayList<JsonObject>();
		JsonObject categoria = null;

		try {
			
			Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);

			while (rs.next()) {

				int idcategoria = rs.getInt("idcategoria");
				String descricao = rs.getString("descricao");

				categoria = new JsonObject();
				categoria.addProperty("idCategoria", idcategoria);
				categoria.addProperty("descricao", descricao);
				
				listaCategoria.add(categoria);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaCategoria;
	}
	
	public boolean deletar (int id) {
			
			String comando = "DELETE FROM categoria WHERE idcategoria = ? ";
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
	
	public Categoria buscarPorId(int id) {
		String comando = "SELECT*FROM categoria WHERE idcategoria = ? ";
		Categoria categoria = new Categoria();
		try {
			PreparedStatement p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {

				int idcategoria = rs.getInt("idcategoria");
				String descricao = rs.getString("descricao");

				categoria.setIdcategoria(idcategoria);
				categoria.setDescricao(descricao);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categoria;
	}
	
	public boolean alterar(Categoria categoria) {

		String comando = "UPDATE categoria " + "SET descricao=? "
				+ " WHERE idcategoria=? ";
		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando);
			p.setString(1, categoria.getDescricao());
			p.setInt(2, categoria.getIdcategoria());
			p.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
