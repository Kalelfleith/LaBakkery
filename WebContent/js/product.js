var PADARIA = new Object();

$(document).ready(function() {

	
			PADARIA.carregarCategoria = function(){
			
			var	select = "#selCategoria";
	
			$.ajax({
				type: "GET",
				url: PADARIA.PATH + "categoria/buscar",
				success: function (categoria) {
						
					$(select).html("");

					var option = document.createElement("option");
					option.setAttribute("value","");
					option.innerHTML = ("Escolha Categoria");
					$(select).append(option);
					
					for (var i = 0; i < categoria.length; i++) {
						
						var option = document.createElement("option");
						option.innerHTML = categoria[i].descricao;
						$(select).append(option);
						
					}
				
					
				},
				error: function (info) {
					PADARIA.exibirAviso("Erro ao tentar buscar as Categorias! "+ info.status + " - " + info.status.Text);
					
					$(select).html("");
					var option = document.createElement("option");
					option.setAttribute ("value", "");
					option.innerHTML = ("Erro ao carregar Categorias!");
					$(select).append(option);
					$(select).addClass("aviso");
		
				}
			});
			
		}
	
	PADARIA.carregarCategoria();
	
	//Cadastra no BD o produto informado
	
	PADARIA.cadastrar = function(){
		
		var produto = new Object ();
		produto.descricao = document.frmAddProduto.descricao.value;
		produto.quantidade = document.frmAddProduto.quantidade.value;
		produto.preco = document.frmAddProduto.preco.value;
		produto.categoria = document.frmAddProduto.categoria.value;
		
		
		if((produto.categoria=="")||(produto.descricao=="")||(produto.quantidade=="")||(produto.valor=="")){
			PADARIA.exibirAviso("Preencha todos os campos!");
			
		}else {
			
			$.ajax({
				type: "POST",
				url: PADARIA.PATH + "produto/inserir",
				data: JSON.stringify(produto),
				success: function (msg) {
					PADARIA.exibirAviso(msg)
					PADARIA.buscar();
					$("#addProduto").trigger("reset");
				},
				error: function (info) {
					PADARIA.exibirAviso("Erro ao cadastrar um novo produto: "+ info.status + " - " + info.statusText);
					alert("Erro ao cadastrar um novo produto!");
				}
			});
			
		}
	}
	
	
	PADARIA.buscar = function (){
		
		var valorBusca = $("#campoBuscaProduto").val();
		
		$.ajax({
			type: "GET",
			url: PADARIA.PATH + "produto/buscar",
			data: "valorBusca="+valorBusca,
			success: function(dados){
				
				dados = JSON.parse(dados);
								
				$("#listaProdutos").html(PADARIA.exibir(dados));
				
			},
			error: function(info){
				PADARIA.exibirAviso("Erro ao consultar os contatos"+ info.status + " - " + info.statusText);
			}
		})
	}
	
	PADARIA.exibir = function (listaDeProdutos) {
		
		var tabela = "<div id='configtabela'>"+
		"<table class='TabelaDeRegistros'>"+
		"<tr >"+
		"<th >Código do Produto</th>" +
		"<th >Categoria</th>" +
		"<th >Descrição</th>" +
		"<th >Preço</th>" +
		"<th >Quantidade</th>" +
		"<th>Ações</th>" +
		"</tr>";
		
		if (listaDeProdutos != undefined && listaDeProdutos.length> 0){
			
			for (var i=0; i<listaDeProdutos.length; i++) {
				
				if(listaDeProdutos[i].quantidade != 0){
					
					tabela += "<tr class='dif'>"+
					"<td id='"+i+"' class='dif'>"+listaDeProdutos[i].idProduto+"</td>"+
					"<td>"+listaDeProdutos[i].categoria+"</td>"+
					"<td>"+listaDeProdutos[i].descricao+"</td>"+
					"<td>R$"+PADARIA.formatarDinheiro(listaDeProdutos[i].preco)+"</td>"+
					"<td>"+listaDeProdutos[i].quantidade+"</td>"+
					"<td>" +
						"<a onclick=\"PADARIA.exibirEdicao('"+listaDeProdutos[i].idProduto+"')\">Editar </a>"+
						"<a onclick=\"PADARIA.excluir('"+listaDeProdutos[i].idProduto+"')\">Excluir</a>"+
					"</td>"+
					"</tr>"
					
				}else {
					
					tabela += "<tr class='dif'>"+
					"<td id='"+i+"' class='dif'>"+listaDeProdutos[i].idProduto+"</td>"+
					"<td>"+listaDeProdutos[i].categoria+"</td>"+
					"<td>"+listaDeProdutos[i].descricao+"</td>"+
					"<td>R$"+PADARIA.formatarDinheiro(listaDeProdutos[i].preco)+"</td>"+
					"<td style='color: red; font-weight: bolder;'>"+"Sem Estoque"+"</td>"+
					"<td>" +
						"<a onclick=\"PADARIA.exibirEdicao('"+listaDeProdutos[i].idProduto+"')\">Editar </a>"+
						"<a onclick=\"PADARIA.excluir('"+listaDeProdutos[i].idProduto+"')\">Excluir</a>"+
					"</td>"+
					"</tr>"
					
				}
				
			};
			
		} else if (listaDeProdutos == "") {
			tabela += "<tr><td colspan='6'>Nenhum Registro Encontrado</td></tr>";
		}
		tabela += "</tabela></div>";
		
		return tabela;
		
	};
	
	//Executa a função de busca ao carregar a página
	PADARIA.buscar();
	
	//Excluir o produto Selecionado
	PADARIA.excluir = function(id) {
		
		$.ajax({
			type:"DELETE",
			url: PADARIA.PATH + "produto/excluir/"+id,
			success: function(msg){
				PADARIA.exibirAviso(msg);
				PADARIA.buscar();
			},
			error: function(info){
				PADARIA.exibirAviso("Erro ao excluir produto: "+ info.status + " - " + info.statusText);
			}
		});
	};
	
	PADARIA.exibirEdicao = function(id){
		
		$.ajax({
			type:"GET",
			url: PADARIA.PATH + "produto/buscar/Id",
			data: "id="+id,
			success: function(produto){
						
				document.frmEditaProduto.idProduto.value = produto.idProduto;
				document.frmEditaProduto.descricao.value = produto.descricao;
				document.frmEditaProduto.quantidade.value = produto.quantidade;
				document.frmEditaProduto.preco.value = produto.preco;
				
				PADARIA.selecionarCategoria(produto.categoria);
				
				var modalEditaProduto = {
					title: "Editar Produto",
					height: 500,
					width: 700,
					modal: true,
					buttons:{
						"Salvar": function(){
						
							PADARIA.editar(produto.idProduto);
							
						},
						"Cancelar": function(){
							$(this).dialog("close");
						}
					},
					close: function(){
					}
				};
				
				$("#modalEditaProduto").dialog(modalEditaProduto);
				
			},
			error: function(info) {
				PADARIA.exibirAviso("Erro ao buscar produto para edição: "+ info.status + " - " + info.statusText);
			}
		})
	}
	
	PADARIA.selecionarCategoria = function(cat){
		
		var select = selCategoriaEdicao;
		
		$.ajax({
			type: "GET",
			url: PADARIA.PATH + "categoria/buscar",
			success: function (categoria) {
					
				$(select).html("");

				var option = document.createElement("option");
				option.setAttribute("value","");
				option.innerHTML = ("Escolha Categoria");
				$(select).append(option);
				
				for (var i = 0; i < categoria.length; i++) {
					
					var option = document.createElement("option");
					
					if(cat===categoria[i].descricao)
						option.setAttribute("selected", categoria.descricao);
					
					option.innerHTML = categoria[i].descricao;
					$(select).append(option);
					
				}
			
				
			},
			error: function (info) {
				PADARIA.exibirAviso("Erro ao tentar buscar as Categorias! "+ info.status + " - " + info.status.Text);
				
				$(select).html("");
				var option = document.createElement("option");
				option.setAttribute ("value", "");
				option.innerHTML = ("Erro ao carregar Categorias!");
				$(select).append(option);
				$(select).addClass("aviso");
	
			}
		});
		
	}
	

	
	PADARIA.editar = function (idProduto){
	
		var produto = new Object();
		
		produto.idProduto = idProduto
		produto.descricao = $("#descricao").val();
		produto.preco = $("#preco").val();
		produto.quantidade = $("#quantidade").val();
		produto.categoria = $("#selCategoriaEdicao").val();
		
		$.ajax({
			type:"PUT",
			url: PADARIA.PATH + "produto/alterar",
			data:JSON.stringify(produto),
			success: function(msg){
				
				PADARIA.exibirAviso(msg);
				PADARIA.buscar();
				$("#modalEditaProduto").dialog("close");
				
			},
			error: function(info){
				PADARIA.exibirAviso("Erro ao editar produto: "+ info.status + " - "+ info.statusText);
			}
		});
		
	}
	
});
	
	


	
	