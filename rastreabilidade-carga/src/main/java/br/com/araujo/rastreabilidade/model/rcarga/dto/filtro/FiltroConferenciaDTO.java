package br.com.araujo.rastreabilidade.model.rcarga.dto.filtro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class FiltroConferenciaDTO extends FiltroBaseDTO {

	private String numeroConferencia;
	private String situacao;
	private String tipoImpressao;
	private String codigoDeposito;
	private String exibeUz;
	
	public FiltroConferenciaDTO(Integer codigoFilial, String descricaoFilial, String situacao, String tipoImpressao,
			String dataInicial, String dataFinal, String codigoDeposito) {
		super();
		this.codigoFilial = codigoFilial;
		this.descricaoFilial = descricaoFilial;
		this.situacao = situacao;
		this.tipoImpressao = tipoImpressao;
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
		this.codigoDeposito = codigoDeposito;
	}
	
	
}