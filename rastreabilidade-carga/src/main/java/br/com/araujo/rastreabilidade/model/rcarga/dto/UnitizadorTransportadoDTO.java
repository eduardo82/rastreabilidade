package br.com.araujo.rastreabilidade.model.rcarga.dto;

import java.math.BigDecimal;
import java.util.Date;

import br.com.araujo.rastreabilidade.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitizadorTransportadoDTO {

	private BigDecimal dataJuliana;
	private BigDecimal sequencialDataJuliana;
	private String tipoProcesso;
	private Long totalItens;
	private Long sumQdtExpedida;
	private String tipoAuditoriaDescricao;
	private String tipoUnitizadorDescricao;
	private String statusUnitizadorVolumoso;
	private String statusLacre1;
	private String statusLacre2;
	private Long totalItensAuditados;
	private Long quantidadeExpedidaSemProblema;
	private Long quantidadeExpedidaAvariada;
	
	private String tipoUnitizador;
	private String numeroUnitizador;
	private Integer id;
	private Integer preNota;
	
	private String codigoBarraObjetoIndevido;
	private String dataConferencia;
	
	
	public UnitizadorTransportadoDTO(BigDecimal dataJuliana, BigDecimal sequencialDataJuliana, String tipoProcesso,
			Integer idPreNfTransportada, Integer id, String numeroUnitizador, String tipoUnitizador, Long totalItens, Long sumQdtExpedida) { 
		
		setDataJuliana(dataJuliana);
		setSequencialDataJuliana(sequencialDataJuliana);
		setTipoProcesso(tipoProcesso);
		setPreNota(idPreNfTransportada);
		setId(id);
		setNumeroUnitizador(numeroUnitizador);
		setTipoUnitizador(tipoUnitizador);
		setTipoUnitizadorDescricao(tipoUnitizador);
		setTotalItens(totalItens);
		setSumQdtExpedida(sumQdtExpedida);
	}
	
	public UnitizadorTransportadoDTO(BigDecimal dataJuliana, BigDecimal sequencialDataJuliana, 
			String tipoProcesso, Integer idPreNfTransportada, Integer id, String numeroUnitizador, 
			Character statusUnitizadorVolumoso, Character statusLacre1, Character statusLacre2,
			String tipoUnitizador, String tipoAuditoria, Long totalItens, Long sumQdtExpedida,
			Long totalItensAuditados, Long quantidadeExpedidaSemProblema, Long quantidadeExpedidaAvariada) { 
		
		setDataJuliana(dataJuliana);
		setSequencialDataJuliana(sequencialDataJuliana);
		setTipoProcesso(tipoProcesso);
		setTipoAuditoriaDescricao(tipoAuditoria);
		setPreNota(idPreNfTransportada);
		setId(id);
		setNumeroUnitizador(numeroUnitizador);
		setStatusUnitizadorVolumoso(statusUnitizadorVolumoso.toString());
		setStatusLacre1(statusLacre1.toString());
		setStatusLacre2(statusLacre2.toString());
		setTipoUnitizador(tipoUnitizador);
		setTotalItens(totalItens);
		setSumQdtExpedida(sumQdtExpedida);
		setTotalItensAuditados(totalItensAuditados);
		setQuantidadeExpedidaSemProblema(quantidadeExpedidaSemProblema);
		setQuantidadeExpedidaAvariada(quantidadeExpedidaAvariada);
	}

	public UnitizadorTransportadoDTO(BigDecimal dataJuliana, BigDecimal sequencialDataJuliana, 
			String tipoProcesso, Integer idPreNfTransportada, Integer id, String numeroUnitizador, 
			Character statusUnitizadorVolumoso, Character statusLacre1, Character statusLacre2,
			String tipoUnitizador, String tipoAuditoria, Long totalItens, Long sumQdtExpedida ) { 
		
		setDataJuliana(dataJuliana);
		setSequencialDataJuliana(sequencialDataJuliana);
		setTipoProcesso(tipoProcesso);
		setTipoAuditoriaDescricao(tipoAuditoria);
		setPreNota(idPreNfTransportada);
		setId(id);
		setNumeroUnitizador(numeroUnitizador);
		setStatusUnitizadorVolumoso(statusUnitizadorVolumoso.toString());
		setStatusLacre1(statusLacre1.toString());
		setStatusLacre2(statusLacre2.toString());
		setTipoUnitizador(tipoUnitizador);
		setTotalItens(totalItens);
		setSumQdtExpedida(sumQdtExpedida);
		setTotalItensAuditados(0L);
		setQuantidadeExpedidaSemProblema(0L);
		setQuantidadeExpedidaAvariada(0L);
	}
	
	public UnitizadorTransportadoDTO(String codigoBarraObjetoIndevido, Date dataConferencia) {
		this.dataConferencia = DateUtils.format(dataConferencia);
		this.codigoBarraObjetoIndevido = codigoBarraObjetoIndevido;
	}
}
