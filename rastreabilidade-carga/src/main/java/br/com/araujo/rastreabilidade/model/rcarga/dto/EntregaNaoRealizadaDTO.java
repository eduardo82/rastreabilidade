package br.com.araujo.rastreabilidade.model.rcarga.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntregaNaoRealizadaDTO {

	private Date dataRotaEntrega;
	private Integer rotaAcompanhamento;
	private Integer codigoFilial;
	private String nomeFilial;
	private String tipoTransportado;
	private String numeroUnitizador;
	private Integer codigoProduto;
	private String descricaoProduto;
	private String descricaoTransportado;
	private String siglaEmbalagemExpedida;
	private Integer qtdeEmbalagemExpedida;
	private Integer qtdeUnitariaExpedida;
}
