package br.com.araujo.rastreabilidade.model.rcarga.dto;

import java.math.BigDecimal;

import br.com.araujo.rastreabilidade.model.rcarga.ProdutoTransportado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProdutoTransportadoDTO extends ProdutoTransportado {

	private static final long serialVersionUID = -288407188882664492L;
	
	private Integer prenota;
	private Integer codigoProduto;
	private BigDecimal digitoProduto;
	private String descricao;
	private Integer sumQtdExpedida;
	private BigDecimal sumQtdRecebida;
	private Integer sumQtdAvariada;
	private Integer sumQtdIndevido;
	private BigDecimal sumQtdFaltante;	
}