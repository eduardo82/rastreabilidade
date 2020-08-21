package br.com.araujo.rastreabilidade.model.rcarga.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoDetalheAcompanhamentoDTO {

	private String titulo;
	private String rota;
	private String codFilial;
	private String nomeFilial;
	private String ordemSaida;
	private String conferencia;
	private String preNota;
	private List<UnitizadorTransportadoDTO> detalhes;
	private List<ProdutoTransportadoDTO> volumosos;
	
	private String totalQtdExpedida;
	private String totalQtdRecebida;
	private String totalQtdAvariada;
	private String totalQtdFaltante;
	private String totalQtdIndevido;
}
