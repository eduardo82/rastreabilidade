package br.com.araujo.rastreabilidade.model.rcarga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControleAuditoriaFilialDTO {

	private Integer idParametro;
	private Integer codigoFilial;
	private String descricaoFilial;
	private String dataInicial;
	private Integer prazoRealizacaoAuditoria;
	private Integer prazoExclusaoArquivos;
	private String audControlados;
	private String audMedicaEspecias;
	private String informarControlados;
	private String informarEspeciais;
	private String transfereArquivos;
}
