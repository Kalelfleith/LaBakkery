$(document).ready (function(){ //Carregar o DOM
	
	var mostraNomeUsuario = sessionStorage.getItem('nomeUsuario');
	//Criamos a Variavel 'mostraNomeUsuario' e atribuímos a ela os dados armazenados
	//na sessionStorage criado no JS do Login.
	
	$(".mostrarNome").text(mostraNomeUsuario);
	//Chamamos a classe 'mostrarNome' declarada no seu HTML e
	//carregamos nela a variável 'mostraNomeUsuario', que armaneza o Nome de Usuário
	
});