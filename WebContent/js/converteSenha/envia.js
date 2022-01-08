function ConverteDadosLogin() {
	
	//Rotina para salvar na SessionStorage o usuario Logado
	var dados = document.formLogin.usuario.value; //Variável dados é criada para armazenar o valor do input "usuario" do seu formulário;
	
	sessionStorage.setItem('nomeUsuario', dados ); //Agora "setamos" na SessionStorage a variavel dados, e atribuímos o nome dessa session como 'nomeUsuario'.
	
	var senhaembase64 = btoa(document.formLogin.senha.value);
	
	document.formLogin.senha.value = senhaembase64;
	
	return true;
	
}