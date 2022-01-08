package LoginServlet;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;

import br.com.labakery.bd.Conexao;
import br.com.labakery.jdbc.JDBCUsuarioDAO;
import br.com.labakery.modelo.Usuario;


@WebServlet("/AutenticacaoServlet")
public class AutenticacaoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private void process(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		
		Usuario dadosautentica = new Usuario();
		dadosautentica.setNome(request.getParameter("usuario"));
		dadosautentica.setSenha(request.getParameter("senha"));
		
		
		
		//String textodeserializado = new String(Base64.decode(request.getParameter("senha")));

		
		//Rotina para criptografar a senha em MD5
		String senmd5 = "";
		MessageDigest md = null;
		
		try {
			md = MessageDigest.getInstance("MD5");
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		BigInteger hash = new BigInteger(1, md.digest(request.getParameter("senha").getBytes()));
		senmd5 = hash.toString(16);
		dadosautentica.setSenha(senmd5);
		//Fim da Rotina + Set. na classe Usuario a senha criptografada.

		Conexao conec = new Conexao();
		Connection conexao = (Connection)conec.abrirConexao();
		
		JDBCUsuarioDAO jdbcAutentica = new JDBCUsuarioDAO(conexao);
		int retorno = jdbcAutentica.consultar(dadosautentica);
		
		//System.out.println("ID do Usuário: "+retorno);
		
		//Se retornar 0 o usuário é inválido;
		if(retorno!=0) {
			
			HttpSession sessao = request.getSession();
			sessao.setAttribute("login", request.getParameter("usuario"));
			response.sendRedirect("Acesso/index.html");
		
			
		}else {
			
			response.sendRedirect("erro.html");
			
		}
		
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		process(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		process(request, response);
	}

}