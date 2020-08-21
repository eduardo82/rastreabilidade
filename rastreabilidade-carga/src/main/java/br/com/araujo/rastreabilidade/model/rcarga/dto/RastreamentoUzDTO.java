package br.com.araujo.rastreabilidade.model.rcarga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RastreamentoUzDTO {

	private String uz;
	private String dataHora;
	private Integer codigoFilial;
	private String descricaoFilial;
	private String situacao;
}
