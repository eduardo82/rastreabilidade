package br.com.araujo.rastreabilidade.model.rcarga.dto;

import java.math.BigDecimal;
import java.util.Date;

import br.com.araujo.rastreabilidade.constates.AppConstantes;
import br.com.araujo.rastreabilidade.model.rcarga.ConferenciaManualRealizada;
import br.com.araujo.rastreabilidade.utils.MascaraOdemSaida;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConferenciaManualRealizadaDTO extends ConferenciaManualRealizada {

	private static final long serialVersionUID = -4975961495490818231L;
	
	private Integer codigoPrenotaFiscalTransportada;
	private Integer codigoPrenota;
	private String tipoProcesso;
	private String conferenciaManual;
	private Integer codigoFilial;
	private String nomeFilial;
	private Date dataExportacao;
	private Integer codigoConferencia;
	private String tipoConferencia;
	private String statusConferencia;
	private Integer codigoConferenciaManual;
	private String ordemSaida;
	private String tipoOrdem;
	private BigDecimal dataJuliana;
	private BigDecimal sequencialDataJuliana;

	private String dataExportacaoView;
	
	public String getTipoOrdem() {
		if ("A ".equals(tipoProcesso)) {			
			tipoOrdem = AppConstantes.TIPO_PROCESSO_PEDIDO_AUTOMATICO;
		} else if ("S ".equals(tipoProcesso)) {			
			tipoOrdem = AppConstantes.TIPO_PROCESSO_PEDIDO_DIRETO;
		}
		return tipoOrdem;
	}
	
	public String getOrdemSaida() {
		ordemSaida = MascaraOdemSaida.ordemSaidaMask(codigoFilial, 
				dataJuliana, 
				sequencialDataJuliana, 
				tipoProcesso);
		return ordemSaida;
	}
}
