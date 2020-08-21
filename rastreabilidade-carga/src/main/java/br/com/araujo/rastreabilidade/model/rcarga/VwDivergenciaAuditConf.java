package br.com.araujo.rastreabilidade.model.rcarga;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="VW_DIVERGENCIA_AUDIT_CONF")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VwDivergenciaAuditConf {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PRME_CD_PRODUTO")
	private Integer codigoProduto;
	
	@Column(name = "PRME_NR_DV", columnDefinition = "numeric")
	private Integer digitoProduto;
	
	@Column(name = "PRME_TX_DESCRICAO1")
	private String descricaoProduto;
	
	@Column(name = "QTDE_DIV_AVARIADA", columnDefinition = "numeric")
	private Integer quantidadeDivergenciaAvariada;
	
	@Column(name = "QTDE_DIV_FALTA", columnDefinition = "numeric")
	private Integer quantidadeDivergenciaFalta;
	
	@Column(name = "QTDE_DIV_LOTE", columnDefinition = "numeric")
	private Integer quantidadeDivergenciaLote;
	
	@Column(name = "QTDE_DIV_EXCESSO", columnDefinition = "numeric")
	private Integer quantidadeDivergenciaExcesso;
	
	@Column(name = "TIPO_DIVERGENCIA")
	private String tipoDivergencia;
	
	@Column(name = "DATA_INICIO")
	private Date dataInicio;
	
	@Column(name = "DATA_FIM")
	private Date dataFim;
	
	@Column(name = "ROTA_SQ_SEQUENCIAL")
	private Integer idRota;
	
	@Column(name = "FILI_CD_FILIAL")
	private Integer filial;
	
	@Column(name = "TIPO")
	private String tipo;
	
	@Column (name = "TIPO_UZ_AUDITORIA", insertable=false, updatable=false)
	private String tipoUZ;
	
	@Column(name="ID_TRANSPORTADO")
	private Integer idTransportado;
	
	@Column(name="CD_RESPONSAVEL", columnDefinition = "int")
	private String cdResponsavel;
	
	@Column(name="NOME_RESPONSAVEL")
	private String nomeResponsavel;
	
	@Column(name = "CLUSTER_APREV", columnDefinition = "smallint")
	private Integer codigoClusterAprev;
	
	@Column (name = "NUMERO")
	private String numero;
	
	@Column (name = "DATA_PEDIDO")
	private Date dataPedido;
	
	@Column (name = "LOTE_EXPEDIDO")
	private String loteExpedido;
	
	@Column (name = "LOTE_CONFERIDO")
	private String loteConferido;
}
