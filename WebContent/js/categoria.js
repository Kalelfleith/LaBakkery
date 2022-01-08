var PADARIA = new Object();

$(document).ready(function() {

			PADARIA.cadastrarCategoria = function(){
				
				var categoria = new Object ();
				categoria.descricao = document.frmAddCategoria.descricao.value;	
				
				if((categoria.descricao=="")){
					PADARIA.exibirAviso("Preencha todos os campos!");
					
				}else {
					
					$.ajax({
						type: "POST",
						url: PADARIA.PATH + "categoria/inserir",
						data: JSON.stringify(categoria),
						success: function (msg) {
							PADARIA.exibirAviso(msg)
							PADARIA.buscarCategoria();
							$("#addCategoria").trigger("reset");
						},
						error: function (info) {
							PADARIA.exibirAviso("Erro ao cadastrar um novo produto: "+ info.status + " - " + info.statusText);
						}
					});
					
				}
			}
			
			PADARIA.buscarCategoria = function (){
				
				var valorBusca = $("#campoBuscaCategoria").val();
				
				$.ajax({
					type: "GET",
					url: PADARIA.PATH + "categoria/buscar",
					data: "valorBusca="+valorBusca,
					success: function(dados){
						
						JSON.stringify(dados);
										
						$("#listaCategorias").html(PADARIA.exibirCategorias(dados));
						
					},
					error: function(info){
						PADARIA.exibirAviso("Erro ao consultar os contatos"+ info.status + " - " + info.statusText);
					}
				})
			}
			
			PADARIA.exibirCategorias = function(listaDeCategorias) {
				
				var tabela = "<div id='configtabela'>"+
				"<table class='TabelaDeRegistros'>" +
				"<tr>" +
				"<th>Código da Categoria</th>" +
				"<th>Categoria</th>" +
				"<th>Ações</th>" +
				"</tr>";
				
				if (listaDeCategorias != undefined && listaDeCategorias.length> 0){
			
					for (var i=0; i<listaDeCategorias.length; i++) {
						tabela += "<tr class='dif'>"+
								"<td id='"+i+"' class='dif'>"+listaDeCategorias[i].idcategoria+"</td>"+
								"<td class='dif'>"+listaDeCategorias[i].descricao+"</td>"+
								"<td class='dif'>" +
									"<a onclick=\"PADARIA.exibirEdicaoCategoria('"+listaDeCategorias[i].idcategoria+"')\">Editar </a>"+
									"<a onclick=\"PADARIA.excluirCategoria('"+listaDeCategorias[i].idcategoria+"')\">Excluir</a>"+
								"</td>"+
								"</tr>"
			
					};
					
				} else if (listaDeCategorias) {
					tabela += "<tr><td colspan='6'>Nenhum Registro Encontrado</td></tr>";
				}
				tabela += "</table></div>";
				
				return tabela;
				
			};
			
			PADARIA.buscarCategoria();
			
			//Excluir a Categoria Selecionado
			PADARIA.excluirCategoria = function(id) {
				
				$.ajax({
					type:"DELETE",
					url: PADARIA.PATH + "categoria/excluir/"+id,
					success: function(msg){
						PADARIA.exibirAviso(msg);
						PADARIA.buscarCategoria();
					},
					error: function(info){
						PADARIA.exibirAviso("Erro ao excluir Categoria: "+ info.status + " - " + info.statusText);
					}
				});
			};
			
			PADARIA.exibirEdicaoCategoria = function(id){
				
				$.ajax({
					type:"GET",
					url: PADARIA.PATH + "categoria/buscarPorId",
					data: "id="+id,
					success: function(categoria){
						
						document.frmEditaCategoria.idCategoria.value = categoria.id;
						document.frmEditaCategoria.descricao.value = categoria.descricao;
						
						
						var modalEditaCategoria = {
								title: "Editar Categoria",
								height: 350,
								width: 700,
								modal: true,
								buttons:{
									"Salvar": function(){
										
										PADARIA.editarCategoria(categoria.idcategoria);
										
									},
									"Cancelar": function(){
										$(this).dialog("close");
									}
								},
								close: function(){

								}
							};
						
							$("#modalEditaCategoria").dialog(modalEditaCategoria);
						
					},
					error: function(info) {
						PADARIA.exibirAviso("Erro ao buscar categoria para edição: "+ info.status + " - " + info.statusText);
					}
				})
			}
			
			PADARIA.editarCategoria = function(idcategoria) {
				
				var categoria = new Object();
				
				categoria.idcategoria = idcategoria;
				categoria.descricao = $("#descricao").val();
				
				$.ajax({
					type:"PUT",
					url: PADARIA.PATH + "categoria/alterar",
					data:JSON.stringify(categoria),
					success: function(msg){
						
						PADARIA.exibirAviso(msg);
						PADARIA.buscar();
						$("#modalEditaCategoria").dialog("close");
						
					},
					error: function(info){
						PADARIA.exibirAviso("Erro ao editar categoria: "+ info.status + " - "+ info.statusText);
					}
				});
				
			}
});
	
	