package Filter;

import LoginServlet.AutenticacaoServlet;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ServletFilter implements Filter{
/*
* O método abstrato init() é exigido por esta classe
* neste caso este método indicará que o Filtro deverá
* executar algo quando o Filtro for carregado pelo
* Container(Servlet).
*/
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain filterChain)throws IOException, ServletException {
	
		String context = request.getServletContext().getContextPath();

		try{
			
			HttpSession session = ((HttpServletRequest)request).getSession();
			
				String usuario = null;
				
					if (session != null){
						
						usuario = (String)session.getAttribute("login");
					}
					
					if (usuario==null) {

						((HttpServletResponse)response).sendRedirect("http://localhost:8080/ProjetoPadaria/erro.html");
						
					}else{
			
						filterChain.doFilter(request,response);
						
					}
		}catch (Exception e){

			e.printStackTrace();
		}
	}//Fechando o bloco do doFilter()
	
	public void destroy() {
	}
}//Fechando a classe
