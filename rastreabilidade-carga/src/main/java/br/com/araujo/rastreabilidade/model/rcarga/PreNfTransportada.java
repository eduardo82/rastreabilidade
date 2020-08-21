package br.com.araujo.rastreabilidade.model.rcarga;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="PRE_NF_TRANSPORTADA")
@Access(AccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PreNfTransportada extends BaseAuditoriaRCarga {

	private static final long serialVersionUID = -9043450707312208928L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "PNFT_SQ_SEQUENCIAL", columnDefinition = "NUMERIC(10,0)")
	private Integer id;
	
	@Column(name = "PNFC_SQ_SEQUENCIAL", nullable=false)
	private Integer preNotaFiscal;
	
	@Column (name = "DEPO_CD_DEPOSITO", nullable=false)
	private Integer codigoDeposito;
	
	@Column (name = "PNFT_FL_ORIGEM", nullable=false, columnDefinition = "char")
	private String flagOrigem;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name = "PNFT_DH_INCLUSAO", nullable=false, length=5)
	private Date dataInclusao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name = "PNFT_DH_EXPORT", nullable=true, length=5)
	private Date dataExportacao;
	
	@Column(name = "FILI_CD_FILIAL", nullable=false)
	private Integer filial;
	
	@Valid
	@OneToMany(mappedBy = "preNotaFiscalTransportada", fetch = FetchType.LAZY)
	private List<ProdutoTransportado> produtoTransportado;	
	
	@Column(name = "ROTA_DT_ROTAENTREGA", insertable = false, updatable = false)
	private Date dataRota;
	
	@Column(name = "ROTA_SQ_SEQUENCIAL", insertable = false, updatable = false)
	private Integer sequencialRota;
	
	@OneToOne(targetEntity = ConferenciaCarga.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "CONF_SQ_SEQUENCIAL", nullable = true)
	private ConferenciaCarga conferencia;
	
	@Column(name = "PNFT_CD_USU_EXPORT", length = 9)
	private Integer cdUsuExport;
	
}
