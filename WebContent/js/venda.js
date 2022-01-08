var PADARIA = new Object();

$(document).ready(function() {
	
	PADARIA.mostrarCalendario = function () {
		$( "#calendario" ).datepicker({dateFormat: 'dd/mm/yy'});
	}
	
	PADARIA.inserirProdutoVenda = function () {
		
		var dataVenda = document.frmConfirmaVenda.calendario.value;
		var IdUsuarioVenda = sessionStorage.getItem('idUsuario');
		
		if(dataVenda==""){
			
			PADARIA.exibirAviso("Preencha a DATA!");
		
		//Validação para Iniciar uma NOVA Venda	
		}else if($("#lista").val() == "" || $("#lista").val() == "[]" || $("#lista").val().length == 0 || $("#lista").val() == undefined){
			
			var venda = new Object ();
			venda.data_venda = PADARIA.formatarData(dataVenda);
			venda.usuario_idUsuario = IdUsuarioVenda;
			
			$.ajax({
				type:"POST",
				url: PADARIA.PATH + "venda/adicionar",
				data: JSON.stringify(venda),
				success: function (retornoIdGerado){
					
					var idDataVenda = retornoIdGerado;
					document.getElementById("idVenda").value = idDataVenda;
					
					PADARIA.exibirModalVenda();
					
				},
				error: function (info){
					PADARIA.exibirAviso("Erro ao cadastrar uma Venda: "+ info.status + " - " + info.statusText);				
				}
			});
				
			
		}else{		
			PADARIA.exibirAviso("Você possui uma Venda PENDENTE!");
			
		}//Fim do Else
		
	}
	
	PADARIA.exibirModalVenda = function () {
		
		if($("#idVenda").val().length == 0 || $("#idVenda").val() == 0 || undefined){//Validar se o input idvenda está vazio, se estiver, siginifca que ainda não foi iniciado uma venda.
			PADARIA.exibirAviso("Inicie uma Venda PRIMEIRO!")
		}else{
			var modalInsereProduto = {
					title: "Novo Item na Venda",
					height: 400,
					width: 775,
					modal: true,
					buttons:{
						"Confirmar": function(){
							
							var quantidadeInserida = $("#quantidade").val();
							var quantidadeEstoque = $("#quantidadeEmEstoque").val();
							quantidadeEstoque = quantidadeEstoque.replace(/[Disponível em Estoque:]+/g, '');
							
							if(quantidadeInserida > quantidadeEstoque){
								PADARIA.exibirAviso("Estoque Insuficiente!");
							}else{
								PADARIA.adicionarProdutoVenda();//INSERE O PRODUTO NA VENDA
							}
							
						},
						"Cancelar": function(){
							$(this).dialog("close");
						}
					},
					close: function(){
					}
			};//Fim da Modal
			$("#modalAdicionaVenda").dialog(modalInsereProduto);
		}//Fim do Else	
	}
	
	PADARIA.carregarProduto = function () {
		
		var	select = "#selProduto";
		
		$.ajax({
			type: "GET",
			url: PADARIA.PATH + "produto/buscarProduto",
			success: function (produto) {

				$(select).html("");
				
				var option = document.createElement("option");
				option.setAttribute("value","");
				option.innerHTML = ("Escolha o Produto");
				$(select).append(option);
					
				for (var i = 0; i < produto.length; i++){
					
					if(produto[i].quantidade != 0){
						
						var option = document.createElement("option");
						option.setAttribute ("value", produto[i].idProduto);
						option.innerHTML = produto[i].descricao;
						$(select).append(option);
						
					}
					
				}
				
			},
			error: function () {
				
				PADARIA.exibirAviso("Erro ao tentar buscar os Produtos! "+ info.status + " - " + info.status.Text);
				
				$(select).html("");
				var option = document.createElement("option");
				option.setAttribute ("value", "");
				option.innerHTML = ("Erro ao carregar Produtos!");
				$(select).append(option);
				$(select).addClass("aviso");
				
			}
			
		});
		
	}
	
	PADARIA.carregarProduto();
	
	PADARIA.selecionaValorSel = function() {
		
		var capturaValorSel = $("#selProduto").val();
		
		$.ajax({
			type: "GET",
			url: PADARIA.PATH + "produto/buscarProdutoEstoque",
			data: "id="+capturaValorSel,
			success: function (produto) {
				
				//Mostrar a quantidade no Input
				document.getElementById("quantidadeEmEstoque").value = "Disponível em Estoque:  "+produto.quantidade;
				
				document.getElementById("valorUn").value = produto.preco;//Armazenar valor no input oculto
				document.getElementById("idProduto").value = produto.idProduto;//Armazenar id no input oculto
				
				},
				error: function (info) {
					PADARIA.exibirAviso("Erro ao Selecionar Produto: "+ info.status + " - "+ info.statusText);
				}
			});//Final do AJAX
		
	}//Final da Função
	
	PADARIA.adicionarProdutoVenda = function () {
			
			var vendaProduto = new Object();
			vendaProduto.venda_idvenda = $("#idVenda").val();
			vendaProduto.produto_idProduto = $("#idProduto").val();
			vendaProduto.produto_quantidade = $("#quantidade").val();
			
			$.ajax({
				type: "POST",
				url: PADARIA.PATH + "venda/adicionarProduto",
				data: JSON.stringify(vendaProduto),
				success: function (msg){
					
					PADARIA.selecionaValorSel();
					PADARIA.exibirAviso(msg);
					PADARIA.buscarProdutoVenda();
					
				},
				error: function(info){
					PADARIA.exibirAviso("Erro ao Inserir Produto na Venda: "+ info.status + " - " + info.statusText);	
				}
			})
	}
	
	PADARIA.buscarProdutoVenda = function () {
		
		var valorBusca = $("#idVenda").val();
			
		$.ajax({
			type: "GET",
			url: PADARIA.PATH + "venda/buscarProdutoVenda",
			data: "valorBusca="+valorBusca,
			success: function(dados){
					
				dados = JSON.parse(dados);
									
				$("#tabelaDeProdutos").html(PADARIA.exibirLista(dados));
					
			},
			error: function(info){
				PADARIA.exibirAviso("Erro ao consultar os contatos"+ info.status + " - " + info.statusText);
			}
		})
	}
	
	PADARIA.exibirLista = function (listaDeProdutos) {
		
		document.getElementById("lista").value = JSON.stringify(listaDeProdutos);//Salva no INPUT "#LISTA" a listaDeProdutos, para poder validar se há alguma venda aberta.
		
		var tabela = "<table class='vendas'>" +
		"<tr>" +
		"<th class='idTab'>#</th>" +
		"<th class='colunaPrincipal'>Produto</th>" +
		"<th>DATA</th>" +
		"<th>Quantidade</th>" +
		"<th>Valor Un</th>" +
		"<th>Ações</th>"+
		"</tr>";
		
		if (listaDeProdutos != undefined && listaDeProdutos.length> 0){
			
			var soma = 0;
			var teste = 0;
			
			for (var i=0; i<listaDeProdutos.length; i++) {
				tabela += "<tr>"+
					"<td class='idTab'>"+listaDeProdutos[i].idProdutoHasVenda+"</td>"+
					"<td class='colunaPrincipal'>"+listaDeProdutos[i].produtoDescricao+"</td>"+
					"<td>"+listaDeProdutos[i].data_venda+"</td>"+
					"<td>"+listaDeProdutos[i].produtoQuantidade+"</td>"+
					"<td>"+PADARIA.formatarDinheiro(listaDeProdutos[i].produtoPreco)+"</td>"+
					"<td>"+
					"<a class='botaoExcluirPV' onclick=\"PADARIA.excluirProdutoVendido('"+listaDeProdutos[i].idProdutoHasVenda+"')\">Excluir</a>"+
					"</td>"
				"</tr>";
				
				soma += (listaDeProdutos[i].produtoPreco*listaDeProdutos[i].produtoQuantidade);
				
			}
			
		}else if(listaDeProdutos){
			tabela += "<tr><td colspan='6' style='text-align: center;'>Nenhum Registro Encontrado</td></tr>";
		}
		
		tabela += "<tr class='borderTr'><td class='borderTd'>Valor Total:  "+PADARIA.formatarDinheiro(soma)+"</td></tr>"+
				"</table></div>";
		
		return tabela;
			
	}
	
	PADARIA.excluirProdutoVendido = function (id) {
		
		$.ajax({
			type: "DELETE",
			url: PADARIA.PATH + "venda/excluirIdProdutoHasVenda/"+id,
			success: function(msg){
				
				PADARIA.selecionaValorSel();
				PADARIA.buscarProdutoVenda();
				
			},
			error: function(info){
				PADARIA.exibirAviso("Erro ao Excluir Item: "+ info.status + " - " + info.statusText);
			}
		})
		
	}
	
	PADARIA.finalizarVenda = function () {
		
		if($("#idVenda").val() == 0){
			PADARIA.exibirAviso("Inicie uma Venda PRIMEIRO!");
		}else{
			document.getElementById("idVenda").value = 0;
			
			PADARIA.buscarProdutoVenda();
			
			PADARIA.exibirAviso("Venda Finalizada com SUCESSO!");
		}	
	}
	
});