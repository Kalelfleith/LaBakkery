var PADARIA = new Object();

$(document).ready(function() {
	
	PADARIA.CalendarioData1 = function () {
		$( "#data1" ).datepicker({dateFormat: 'dd/mm/yy'});
	}
	
	PADARIA.CalendarioData2 = function () {
		$( "#data2" ).datepicker({dateFormat: 'dd/mm/yy'});
	}
	
	/*
	PADARIA.produtoMaisVendido = function () {
		$.ajax({
			type: "GET",
			url: PADARIA.PATH + "faturamento/produtoMaisVendido",
			success: function (listaResultado) {
				
				JSON.stringify(listaResultado);
				
				console.log(listaResultado);
				
				$("#listaProdutosVendidos").html(PADARIA.exibirConsulta(listaResultado));
				
			},
			error: function () {
				
			}
		})
	}
	
	PADARIA.exibirConsulta = function (listaResultado){
		
		var tabela = "<div id='configtabelaFaturamento'>"+
		"<table class='TabelaDeRegistros'>" +
		"<tr>" +
		"<th>Id</th>" +
		"<th>Produto</th>" +
		"<th>Quantidade de Vendas</th>" +
		"<th>Faturamento</th>"+
		"</tr>";
		
		if (listaResultado != undefined && listaResultado.length> 0){
			
			for (var i=0; i<listaResultado.length; i++) {
				tabela += "<tr>"+
						"<td>"+listaResultado[i].produto_idProduto+"</td>"+
						"<td>"+listaResultado[i].descricao+"</td>"+
						"<td>"+listaResultado[i].qtdTotal+"</td>"+
						"<td>R$"+PADARIA.formatarDinheiro(listaResultado[i].valorTotal)+"</td>"+
						"</td>"+
						"</tr>"
	
			};
			
		}else if (listaResultado) {
			tabela += "<tr><td colspan='6'>Nenhum Registro Encontrado</td></tr>";
		}
		
		tabela += "</table></div>";
		
		return tabela;
		
	}
	*/
	
	PADARIA.VendasPorPeriodo = function () {
		
		var datas = new Object();
		datas.data1 = PADARIA.formatarData($("#data1").val());
		datas.data2 = PADARIA.formatarData($("#data2").val());
		
		if(datas.data1=="" || datas.data2==""){
			
			PADARIA.exibirAviso("Preencha a DATA!");
		}else{
			
			$.ajax({
				type: "POST",
				url: PADARIA.PATH + "faturamento/porPeriodo",
				data: JSON.stringify(datas),
				success: function (dados) {
					
					JSON.stringify(dados);
					
					PADARIA.exibirConsultaPorPeriodo(dados);
					
				},
				error: function (){
					
				}
			})//Fim do ajax
			
		}//Fim do Else
		
	}//Fim da Função
	
	PADARIA.exibirConsultaPorPeriodo = function (listaResultado){
		
		console.log(listaResultado);
		
		var tabela = "<div id='configtabelaFaturamento'>"+
		"<table style='border: 1px solid black; padding: 8px; padding-top: 12px; padding-bottom: 12px; text-align: center; color:black;'>" +
		"<tr>" +
		"<th style='border-bottom: 1px solid black; padding: 8px;'>Cód</th>" +
		"<th style='border-bottom: 1px solid black; padding: 8px;'>Produto</th>" +
		"<th style='border-bottom: 1px solid black; padding: 8px;'>DATA</th>" +
		"<th style='border-bottom: 1px solid black; padding: 8px;'>Quantidade de Vendas</th>" +
		"<th style='border-bottom: 1px solid black; padding: 8px;'>Faturamento de Cada Produto</th>"+
		"</tr>";
		
		if (listaResultado != undefined && listaResultado.length> 0){
			
			var soma = 0;
			
			for (var i=0; i<listaResultado.length; i++) {
				tabela += "<tr>"+
						"<td>"+listaResultado[i].produto_idProduto+"</td>"+
						"<td>"+listaResultado[i].descricao+"</td>"+
						"<td>"+PADARIA.reFormatarData(listaResultado[i].dataVenda)+"</td>"+
						"<td>"+listaResultado[i].qtdTotal+"</td>"+
						"<td>R$"+PADARIA.formatarDinheiro(listaResultado[i].valorTotal)+"</td>"+
						"</td>"+
						"</tr>"
						
						soma += listaResultado[i].valorTotal;
	
			}
			
		}else if (listaResultado) {
			tabela += "<tr><td colspan='5'>Nenhum Venda Encontrada</td></tr>";
		}
		
		tabela += "<tr><td colspan='5' style='border: 1px solid black;'>Valor Total Faturado: R$"+PADARIA.formatarDinheiro(soma)+"</td></tr>"
		tabela += "</table></div>";
		
		var janela = window.open("http://google.com", "_blank","width=900, height=900");
		
		janela.document.write("<body>")+
		janela.document.write("<header style='background-color: #fff; margin: 0 auto; height: 75px; width: 100%;  border-bottom: 2px solid #161b22;'")+//Inicio do Header
		janela.document.write("<div style='background: url(../imgs/logoRelatorio.png); margin-top: 0px; margin-left: 5px; float: left; height: 70px; width: 100px;'></div>")+
		janela.document.write("<table style='margin-top: 0px; margin-left: 25%; padding: 10px; color: #161b22; text-align: left;'")+
		janela.document.write("<tr>")+
		janela.document.write("<th>Nome do Usuário: </th>")+
		janela.document.write("<th>Cargo: </th>")+
		janela.document.write("</tr>")+
		janela.document.write("<tr>")+
		janela.document.write("<td>Kalel Fleith</td>")+
		janela.document.write("<td>Gerente</td>")+
		janela.document.write("</tr>")+
		janela.document.write("</table>")+
		janela.document.write("</div>")+
		janela.document.write("</header>")+//Fim do Header
		janela.document.write("<div style='width: 100%; text-align: center;'><h2>Relatório de Faturamento</h2></div>")+
		janela.document.write(tabela)+
		janela.document.write("<footer style='color: #161b22; border-top: 2px solid #161b22; text-align: center; padding: 26px; position: fixed; bottom: 0; width: 100%;'>&copy;Desenvolvido por LaBakery</footer>")+
		janela.document.write("</body>");//Fim do body
		
		janela.print();
		janela.document.close();

	}
	
})