package br.com.araujo.rastreabilidade.model.rcarga.dto;

import java.util.Date;

import br.com.araujo.rastreabilidade.model.rcarga.ConferenciaCarga;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConferenciaDTO extends ConferenciaCarga {

	private static final long serialVersionUID = -3875447336120545107L;
	
	private Integer codigoFilial;
	private String nomeFilial;
	private String dataAberturaView;
	private String dataFechamentoParcialView;
	private String dataFechamentoView;
	private String dataCancelamentoView;
	private String situacaoView;
	private String tipoImpressaoView;
	
	
	public ConferenciaDTO(Integer id, Date dataAbertura, Date dataFechamento, Date dataFechamentoParcial, Integer codigoFilial,
			String nomeFilial, Date dataImpressao, Date dataReimpressao) {
		this.setId(id);
		this.setDataAbertura(dataAbertura);
		this.setDataFechamento(dataFechamento);
		this.setDataFechamentoParcial(dataFechamentoParcial);
		this.setCodigoFilial(codigoFilial);
		this.setNomeFilial(nomeFilial);
		this.setDataImpressao(dataImpressao);
		this.setDataReimpressao(dataReimpressao);
	}
}
