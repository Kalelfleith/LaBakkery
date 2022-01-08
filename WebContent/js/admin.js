var PADARIA = new Object();

$(document).ready(function (){
	
	PADARIA.PATH = "/ProjetoPadaria/rest/";
		
	PADARIA.buscarPaginaProduto = function() {
		if(sessionStorage.getItem('cargoUsuario') != "Caixa"){
			PADARIA.carregaPagina();
			$('#ambientehtml').load("/ProjetoPadaria/Pages/produto.html");
			PADARIA.carregarCategoria();
			PADARIA.buscar();
		}else{
			PADARIA.exibirAviso("Você NÃO possui PERMISSÃO para acessar esse processo!");
		}
		
	}
	
	PADARIA.buscarPaginaCategoria = function() {
		if(sessionStorage.getItem('cargoUsuario') != "Caixa"){
			PADARIA.carregaPagina();
			$('#ambientehtml').load("/ProjetoPadaria/Pages/categoria.html");
			PADARIA.buscarCategoria();
		}else{
			PADARIA.exibirAviso("Você NÃO possui PERMISSÃO para acessar esse processo!");
		}
		
	}
	
	PADARIA.buscarPaginaUsuario = function() {
		if(sessionStorage.getItem('cargoUsuario') != "Caixa"){
			PADARIA.carregaPagina();
			$('#ambientehtml').load("/ProjetoPadaria/Pages/usuario.html");
			PADARIA.buscarUsuario();
		}else{
			PADARIA.exibirAviso("Você NÃO possui PERMISSÃO para acessar esse processo!");
		}
		
	}
	
	PADARIA.buscarPaginaVendas = function() {
		PADARIA.carregaPagina();
		$('#ambientehtml').load("/ProjetoPadaria/Pages/menuVendas.html");
		PADARIA.carregarProduto();
	}
	
	PADARIA.buscarPaginaFaturamento = function () {
		if(sessionStorage.getItem('cargoUsuario') != "Caixa"){
			$('#ambientehtml').load("/ProjetoPadaria/Pages/faturamento.html");
		}else{
			PADARIA.exibirAviso("Você NÃO possui PERMISSÃO para acessar esse processo!");
		}
		
	}
	
	//exibe os vlaores financeiros no formato da moeda Real

    PADARIA.formatarDinheiro = function(preco) {
    	if(preco == undefined){
    		preco = 0;
    	}
        return preco.toFixed(2).replace('.', ',').replace(/(\d)(?=(\d{3})+\,)/g, "$1.");
    }
    
    PADARIA.formatarData = function(dataVenda){
    	return dataVenda.split('/').reverse().join('-');
    }
    
    PADARIA.reFormatarData = function(dataVenda){
    	return dataVenda.split('-').reverse().join('/');
    }
    
    PADARIA.carregaPagina = function() {
        if ($(".ui-dialog"))
            $(".ui-dialog").remove();
    }
    
    PADARIA.exibirAviso = function(aviso){
		var modal = {
				title: "Mensagem",
				height: 250,
				width: 400,
				modal: true,
				buttons: {
					"OK": function(){
						$(this).dialog("close");
					}
				}
		};
		$("#modalAviso").html(aviso);
		$("#modalAviso").dialog(modal);
	};
	
	PADARIA.verificarUsuario = function (){
		var valorBusca = sessionStorage.getItem('nomeUsuario');
		//Requisição para buscar o ID do usuário
		$.ajax({
			type: "GET",
			url: PADARIA.PATH + "venda/buscarIdUsuario",
			data: "valorBusca="+valorBusca,
			success: function (dados){
				
				var IdUsuarioLogado = dados.idUsuario;
				var cargoUsuario = dados.cargo;
				
				sessionStorage.setItem('idUsuario', IdUsuarioLogado );
				sessionStorage.setItem('cargoUsuario', cargoUsuario );
				
			},
			error: function (info){
				PADARIA.exibirAviso("Erro ao Buscar ID do Usuário: "+ info.status + " - " + info.statusText);
			}
			
		})
	}
	
	PADARIA.verificarUsuario();
	
});





