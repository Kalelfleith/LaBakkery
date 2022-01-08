package br.com.labakery.jdbc;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import br.com.labakery.jdbcinterface.UsuarioDAO;
import br.com.labakery.modelo.Produto;
import br.com.labakery.modelo.Usuario;
import br.com.labakery.modelo.ValidaCadastro;

public class JDBCUsuarioDAO implements UsuarioDAO {
	
	private Connection conexao;
	
	public JDBCUsuarioDAO(Connection conexao) {
		this.conexao = conexao;
		
	}
	
	public int verificaUsuario (ValidaCadastro dadoscadastro) {
		
		String comando = "SELECT * FROM usuario WHERE nome LIKE '"+dadoscadastro.getNome()+"' OR matricula='"+dadoscadastro.getMatricula()+"'";
		int verifica = 1;
		
		try {
			Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);
			if(rs.next()) {
				verifica = 0;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return verifica;
	}
	
	public boolean inserir(Usuario usuario){
		
		String comando = "INSERT INTO usuario (idUsuario, nome, matricula, senha, cargo) VALUES (?,?,?,?,?)";
	
		PreparedStatement p;
	
		try {
		
			p = this.conexao.prepareStatement(comando);
			
			p.setInt(1, usuario.getIdUsuario());
			p.setString(2, usuario.getNome());
			p.setInt(3, usuario.getMatricula());
			p.setString(4, usuario.getSenha());
			p.setString(5, usuario.getCargo());
			
			p.execute();			
		
		
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	

	}
	
	public List<JsonObject> buscarPorNome(String nome) {
		
		String comando = "SELECT * FROM usuario WHERE nome LIKE '%"+ nome + "%'";
		
		List<JsonObject> listaUsuario = new ArrayList<JsonObject>();
		JsonObject usuario = null;

		try {
			
			Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);

			while (rs.next()) {

				int idusuario = rs.getInt("idusuario");
				String nome1 = rs.getString("nome");
				String cargo = rs.getString("cargo");
				String senha = rs.getString("senha");

				usuario = new JsonObject();
				usuario.addProperty("idUsuario", idusuario);
				usuario.addProperty("nome", nome1);
				usuario.addProperty("cargo", cargo);
				usuario.addProperty("senha", senha);
				
				
				listaUsuario.add(usuario);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaUsuario;
	}
	
		public boolean deletar (int id) {
			
			String comando = "DELETE FROM usuario WHERE idusuario = ? ";
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
		
		public Usuario buscarPorId(int id) {
			String comando = "SELECT*FROM usuario WHERE idUsuario = ? ";
			Usuario usuario = new Usuario();
			try {
				PreparedStatement p = this.conexao.prepareStatement(comando);
				p.setInt(1, id);
				ResultSet rs = p.executeQuery();
				while (rs.next()) {

					int idUsuario = rs.getInt("idUsuario");
					String nome = rs.getString("nome");
					String cargo = rs.getString("cargo");
					String senha = rs.getString("senha");

					usuario.setIdUsuario(idUsuario);
					usuario.setNome(nome);
					usuario.setCargo(cargo);
					usuario.setSenha(senha);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return usuario;
		}
		
		public boolean alterar(Usuario usuario) {

			String comando = "UPDATE usuario " + "SET nome=?, cargo=?, senha=?"
					+ " WHERE idUsuario=? ";
			PreparedStatement p;

			try {
				p = this.conexao.prepareStatement(comando);
				p.setString(1, usuario.getNome());
				p.setString(2, usuario.getCargo());
				p.setString(3, usuario.getSenha());
				p.setInt(4, usuario.getIdUsuario());
				p.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		public int consultar(Usuario dadosautentica) {
			
			try {
				
				String comando = "SELECT idUsuario FROM usuario WHERE nome = '"+dadosautentica.getNome()+"' and senha = '"+ dadosautentica.getSenha()+"'";
				java.sql.Statement stmt = conexao.createStatement();
				ResultSet rs = stmt.executeQuery(comando);
				if(rs.next()) {
					int idUsuario = rs.getInt("idUsuario");
					//System.out.println(idUsuario);
					dadosautentica.setIdUsuario(idUsuario);
					return idUsuario;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return 0;
			
		}	
}
