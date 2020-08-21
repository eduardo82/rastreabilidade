package br.com.araujo.rastreabilidade.model.rcarga.dto.filtro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FiltroDivergenciaAuditoriaDTO extends FiltroBaseDTO {

	private Integer codigoCluster;
	private String tipoUzVolumoso;
	private String tipoUz;
	private String antibiotico;
	private Integer codigoProduto;
	private String tipoDivergencia;
	private String orderBy;
	
	private Integer codigoProdutoDetalhe;
	private String descricaoProdutoDetalhe;
}
