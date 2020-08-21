package br.com.araujo.rastreabilidade.model.rcarga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcompanhamentoCargaDTO {

	private String farolNaPresenca;
	private String farolAposLiberacao;
	private Boolean existeFarol;
	private String dataExpedicao;
	private Long filialAcompanhamento;
	private String nomeFantasia;
	private String pedido;
	private Long rotaAcompanhamento;
	private Long sequencialRotaAcompanhamento;
	private Long preNota;
	private Long preNotaCab;
	private Long conferenciaAcompanhamento;
	private String dataAbertura;
	private String dataFechamento;
	private String dataFechamentoParcial;
	private String dataCancelamento;
	private Long quantidadeUzExpedido;
	private Long quantidadeUzRecebido;
	private Long quantidadeUzAvariado;
	private Long quantidadeUzFaltantes;
	private Long quantidadeUzIndevidos;
	private Long quantidadeAuditoriaPlanejada;
	private Long quantidadeAuditoriaRealizada;
	private Long quantidadeAuditoriaPlanejadaNaoRealizada;
	private Long quantidadeAuditoriaNaoPlanejadaNaoRealizada;
	private Long quantidadeAuditoriaNaoRealizadaTotal;
	private Long quantidadeAuditoriaItensComDivergencia;
	private Long quantidadeAuditoriaItensRealizadaForaDoPrazo;
	private Long quantidadeItensConferenciaManual;
	private Long quantidadeVolumosoExpedido;
	private Long quantidadeVolumosoRecebido;
	private Long quantidadeVolumosoAvariado;
	private Long quantidadeVolumosoFaltante;
	private Long quantidadeVolumosoIndevido;
	private Long quantidadeCancelada;
	private String statusConferencia;
	private String ordemDeSaida;
	private String tipoUnitizadorFiltroDetalhe = "";
}
