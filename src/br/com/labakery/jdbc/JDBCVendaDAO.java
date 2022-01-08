package br.com.labakery.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import br.com.labakery.jdbcinterface.VendaDAO;
import br.com.labakery.modelo.Venda;
import br.com.labakery.modelo.Produto;
import br.com.labakery.modelo.ProdutoHasVenda;
import br.com.labakery.modelo.Usuario;

public class JDBCVendaDAO implements VendaDAO{
	
	private Connection conexao;
	
	public JDBCVendaDAO(Connection conexao) {
		this.conexao = conexao;
	}
	
	public int adicionar(Venda venda){
		
		String comando = "INSERT INTO venda (idvenda, data_venda, usuario_idUsuario) VALUES (?,?,?)";
		String obterIdGerado = "SELECT LAST_INSERT_ID()"; //SQL traz o último id inserido.
	
		PreparedStatement p;
	
		try {
			
			java.sql.Statement stmt = conexao.createStatement();
			
			p = this.conexao.prepareStatement(comando);
			
			p.setInt(1, venda.getIdvenda());
			p.setString(2, venda.getDataVenda());
			p.setInt(3, venda.getUsuario());
			p.execute();
			ResultSet rs = stmt.executeQuery(obterIdGerado);
			if(rs.next()) {
				int idGerado = rs.getInt("LAST_INSERT_ID()");
				return idGerado;
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;

	}
	
	public Usuario buscarIdUsuario(String nome) {
		
		String comando = "SELECT idUsuario, cargo  FROM usuario WHERE nome = '"+nome+"'";
		
		Usuario usuario = new Usuario();
		
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);
			if(rs.next()) {
				int idUsuario = rs.getInt("idUsuario");
				String cargo = rs.getString("cargo");
				
				usuario.setIdUsuario(idUsuario);
				usuario.setCargo(cargo);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return usuario;
		
	}
	
	public boolean adicionarProduto(ProdutoHasVenda vendaProduto){
		
		String comando = "INSERT INTO produto_has_venda (idproduto_has_venda, venda_idvenda, produto_idProduto, produto_quantidade) VALUES (?,?,?,?)";
		
		String comandoSub = "UPDATE produto SET quantidade = quantidade - (?) WHERE idproduto = (?)";
	
		PreparedStatement p;
		PreparedStatement s;
	
		try {
		
			p = this.conexao.prepareStatement(comando);
			s = this.conexao.prepareStatement(comandoSub);
			
			p.setInt(1, vendaProduto.getIdproduto_has_venda());
			p.setInt(2, vendaProduto.getVenda_idvenda());
			p.setInt(3, vendaProduto.getProduto_idProduto());
			p.setInt(4, vendaProduto.getProduto_quantidade());
			p.execute();	
			
			s.setInt(1, vendaProduto.getProduto_quantidade());
			s.setInt(2, vendaProduto.getProduto_idProduto());
			s.execute();
		
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	

	}
	
	public List<JsonObject> buscarProdutoVenda(int valorBusca) {
		
		String comando = "SELECT idproduto_has_venda, produto.descricao, data_venda, produto_quantidade, produto.preco  FROM produto_has_venda"
				+ " INNER JOIN produto ON produto_idProduto = idProduto"
				+ " INNER JOIN venda ON venda_idVenda = idvenda WHERE venda_idVenda = '"+valorBusca+"'";
	
		
		List<JsonObject> listaProduto = new ArrayList<JsonObject>();
		
		JsonObject produtoVendido = null;
		
		try {
			
			Statement stmt = conexao.createStatement();
			
			ResultSet rs = stmt.executeQuery(comando);
			
			while (rs.next()) {
				
				int idProdutoHasVenda = rs.getInt("idproduto_has_venda");
				String descricao = rs.getString("produto.descricao");
				String data = rs.getString("data_venda");
				int quantidade = rs.getInt("produto_quantidade");
				float preco = rs.getFloat("produto.preco");
				
				produtoVendido = new JsonObject();
				produtoVendido.addProperty("idProdutoHasVenda", idProdutoHasVenda);
				produtoVendido.addProperty("produtoDescricao", descricao);
				produtoVendido.addProperty("data_venda", data);
				produtoVendido.addProperty("produtoQuantidade", quantidade);
				produtoVendido.addProperty("produtoPreco", preco);
				
				listaProduto.add(produtoVendido);
				
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return listaProduto;
	}
	
	public boolean deletarIdVenda (int id) {
		
		String comandoCaptura = "SELECT produto_idProduto, produto_quantidade FROM produto_has_venda WHERE idproduto_has_venda = '"+id+"'";
		String comandoSoma = "UPDATE produto SET quantidade = quantidade + (?) WHERE idproduto = (?)";
		String comando = "DELETE FROM produto_has_venda WHERE idproduto_has_venda =?";
		
		PreparedStatement p;
		PreparedStatement soma;
		try {
			
			Statement stmt = conexao.createStatement();
			
			ResultSet rs = stmt.executeQuery(comandoCaptura);
			
			if(rs.next()) {
				
				int idProduto = rs.getInt("produto_idProduto");
				int produtoQuantidade = rs.getInt("produto_quantidade");	
				
				soma =  this.conexao.prepareStatement(comandoSoma);
				
				soma.setInt(1, produtoQuantidade);
				soma.setInt(2, idProduto);
				soma.executeUpdate();
			}	
			
			p =  this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			p.execute();	
			
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
	}

}
