package br.com.araujo.rastreabilidade.model.rcarga.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DivergenciaAuditoriaDTO {

	private Integer codigoFilial;
	private Integer codigoProduto;
	private Integer digitoProduto;
	private String descricaoProduto;
	private String descricaoFilial;
	private Long quantidadeDivergenciaAvariada;
	private Long quantidadeDivergenciaFalta;
	private Long quantidadeDivergenciaLote;
	private Long quantidadeDivergenciaExcesso;
	private Long quantidadeDivergenciaTotal;

	private BigDecimal quantidadeAvariadaNative;
	private BigDecimal quantidadeFaltaNative;
	private BigDecimal quantidadeLoteNative;
	private BigDecimal quantidadeExcessoNative;
	private BigDecimal quantidadeTotalNative;
	private BigDecimal digitoProdutoNative;
}
