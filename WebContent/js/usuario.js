var PADARIA = new Object();

$(document).ready(function() {
		
		PADARIA.cadastrarUsuario = function(nwsenha){
			
			var usuario = new Object ();
			usuario.nome = document.frmAddUsuario.nome.value;
			usuario.matricula = document.frmAddUsuario.matricula.value;
			usuario.cargo = document.frmAddUsuario.cargo.value;
			
			var login = new Object ();
			login.nome = document.frmAddUsuario.nome.value;
			login.matricula = document.frmAddUsuario.matricula.value;
			
			var confirmaSenha = document.frmAddUsuario.confirmasenha.value;
			var senha = document.frmAddUsuario.senha.value;
			
			if((usuario.nome=="")||(usuario.cargo=="")||(usuario.senha==""||(confirmaSenha==""))){
				PADARIA.exibirAviso("Preencha todos os campos!");
				
			}else if(senha != confirmaSenha){
				
				PADARIA.exibirAviso("Senhas NÃO Conferem!!");
				document.getElementById("senha").focus();
				
			}else{
				$.ajax({
					type: "POST",
					url: PADARIA.PATH + "usuario/verificaUsuario",
					data: JSON.stringify(login),
					success: function (data) {
						if(data==0){
							PADARIA.exibirAviso("Usuário Já Cadastrado !!");
						}else{
							
							var senhaembase64 = btoa(document.frmAddUsuario.senha.value);
							
							document.frmAddUsuario.senha.value = senhaembase64;
							
							usuario.senha = senhaembase64;
							
							$.ajax({
								type: "POST",
								url: PADARIA.PATH + "usuario/inserir",
								data: JSON.stringify(usuario),
								success: function (msg) {
									PADARIA.exibirAviso(msg)
									PADARIA.buscarUsuario();
									$("#addUsuario").trigger("reset");
							
								},
								error: function (info) {
									PADARIA.exibirAviso("Erro ao cadastrar um novo Usuário: "+ info.status + " - " + info.statusText);
								}
							});	
							
						}
				
					},
					error: function (info) {
						PADARIA.exibirAviso("Erro ao cadastrar um novo Usuário: "+ info.status + " - " + info.statusText);
					}
				});		
			}
		}
		
		PADARIA.buscarUsuario = function(){
			
			var valorBusca = $("#campoBuscaUsuario").val();
			
			$.ajax({
				type: "GET",
				url: PADARIA.PATH + "usuario/buscar",
				data: "valorBusca="+valorBusca,
				success: function(dados){
					
					dados = JSON.parse(dados);
									
					$("#listaUsuarios").html(PADARIA.exibirUsuarios(dados));
					
				},
				error: function(info){
					PADARIA.exibirAviso("Erro ao consultar os Usuarios"+ info.status + " - " + info.statusText);
				}
			})
			
		};
		
		
		PADARIA.exibirUsuarios = function (listaDeUsuarios) {
			
			var tabela = "<div id='configtabela'>"+
			"<table class='TabelaDeRegistros'>" +
			"<tr>" +
			"<th>Código do Usuário</th>" +
			"<th>Usuário</th>" +
			"<th>Cargo</th>" +
			"<th>Ações</th>" +
			"</tr>";
			
			if (listaDeUsuarios != undefined && listaDeUsuarios.length> 0){
				
				for (var i=0; i<listaDeUsuarios.length; i++) {
					tabela += "<tr class='dif'>"+
							"<td id='"+i+"' class='dif'>"+listaDeUsuarios[i].idUsuario+"</td>"+
							"<td>"+listaDeUsuarios[i].nome+"</td>"+
							"<td>"+listaDeUsuarios[i].cargo+"</td>"+
							"<td>" +
								"<a onclick=\"PADARIA.exibirEdicaoUsuario('"+listaDeUsuarios[i].idUsuario+"')\">Editar     </a>"+
								"<a onclick=\"PADARIA.excluirUsuario('"+listaDeUsuarios[i].idUsuario+"')\">        Excluir</a>"+
							"</td>"+
							"</tr>"
				};
				
			} else if (listaDeUsuarios) {
				tabela += "<tr><td colspan='6'>Nenhum Registro Encontrado</td></tr>";
			}
			tabela += "</tabela></div>";
			
			return tabela;
		}
		
		PADARIA.buscarUsuario();
		
		
		PADARIA.excluirUsuario = function(id) {
			
			$.ajax({
				type:"DELETE",
				url: PADARIA.PATH + "usuario/excluir/"+id,
				success: function(msg){
					PADARIA.exibirAviso(msg);
					PADARIA.buscarUsuario();
				},
				error: function(info){
					PADARIA.exibirAviso("Erro ao excluir Usuario: "+ info.status + " - " + info.statusText);
				}
			});
		};
		
		
		PADARIA.exibirEdicaoUsuario = function(id){
			
			$.ajax({
				type:"GET",
				url: PADARIA.PATH + "usuario/buscarPorId",
				data: "id="+id,
				success: function(usuario){
					
					console.log(usuario);
									
					document.frmEditaUsuario.idUsuario.value = usuario.id;
					document.frmEditaUsuario.nome.value = usuario.nome;
					document.frmEditaUsuario.senha.value = usuario.senha;
					document.frmEditaUsuario.cargo.value = usuario.cargo;
					
					PADARIA.selecionarSetor(usuario.cargo);
					
					
					var modalEditaUsuario = {
							title: "Editar Usuario",
							height: 350,
							width: 700,
							modal: true,
							buttons:{
								"Salvar": function(){
									
									PADARIA.editarUsuario(usuario.idUsuario);
									
								},
								"Cancelar": function(){
									$(this).dialog("close");
								}
							},
							close: function(){
					
							}
						};
					
						$("#modalEditaUsuario").dialog(modalEditaUsuario);
					
				},
				error: function(info) {
					PADARIA.exibirAviso("Erro ao buscar cargo para edição: "+ info.status + " - " + info.statusText);
				}
			})
		}
		
		
		PADARIA.selecionarSetor = function (set){
			
			var select = "#selCargoEdicao";
			var sel1 = "Gerente";
			var sel2 = "Caixa";
			
				
			$(select).html("");
			var option = document.createElement("option");
			option.setAttribute("value","");
			option.innerHTML = set;
			$(select).append(option);
			
			for(var i=0; i < 1; i++){
				
				var option = document.createElement("option");
				
				if (set==="Gerente"){
					
					option.innerHTML = sel2;
					$(select).append(option);
					
				}else{
					option.innerHTML = sel1;
					$(select).append(option);
				}
				
			}
			
		}
		
		PADARIA.editarUsuario = function (idUsuario) {
			
			var usuario = new Object();
			
			usuario.idUsuario = idUsuario;
			usuario.nome = $("#nome").val();
			usuario.senha = $("#senha").val();
			usuario.cargo = $("#selCargoEdicao").val();
			
			$.ajax({
				type:"PUT",
				url: PADARIA.PATH + "usuario/alterar",
				data:JSON.stringify(usuario),
				success: function(msg){
					
					PADARIA.exibirAviso(msg);
					PADARIA.buscarUsuario();
					$("#modalEditaUsuario").dialog("close");
					
				},
				error: function(info){
					PADARIA.exibirAviso("Erro ao editar Usuário: "+ info.status + " - "+ info.statusText);
				}
			});
			
		}
		
});