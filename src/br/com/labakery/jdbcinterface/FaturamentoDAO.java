package br.com.labakery.jdbcinterface;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.labakery.modelo.ConsultaData;

public interface FaturamentoDAO{
	
	public List <JsonObject> buscarProdutoMaisVendido();
	public List<JsonObject> consultaVendaPorData(ConsultaData valorData);

}
