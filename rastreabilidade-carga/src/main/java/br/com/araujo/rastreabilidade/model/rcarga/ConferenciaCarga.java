package br.com.araujo.rastreabilidade.model.rcarga;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="CONFERENCIA_CARGA")
@Access(AccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConferenciaCarga extends BaseAuditoriaRCarga {

	private static final long serialVersionUID = -7828408510759543504L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name = "CONF_SQ_SEQUENCIAL", nullable=false)
	private Integer id;

	@Column (name = "CONF_CD_USU_ABERT", length = 9 )
	private Integer cdUsuAbert;
	
	@Column (name = "CONF_CD_USU_FECH", length = 9 )
	private Integer cdUsuFech;
	
	@Column(name = "CONF_DH_ABERTURA")
	private Date dataAbertura;
	
	@Column (name = "CONF_DH_FECHAMENTO")
	private Date dataFechamento;
	
	@Column (name = "CONF_DH_FECHAMENTO_PARCIAL")
	private Date dataFechamentoParcial;
	
	@Column (name = "CONF_DH_CANCELAMENTO")
	private Date dataCancelamento;
	
	@Column(name = "CONF_CD_USUCANCEL", length = 9)
	private Integer responsavelCancelamento;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name = "CONF_DH_INCLUSAO")
	private Date dataInclusao;
	
	@Column (name = "CONF_FL_TIPO", columnDefinition = "char")
	private String flagTipo;	
	
	@Column (name = "CONF_FL_STATUS", columnDefinition = "char")
	private String flagStatus;	
	
	@Column(name = "CONF_DH_IMPRESSAO")
	private Date dataImpressao;
	
	@Column(name = "CONF_DH_REIMPRESSAO")
	private Date dataReimpressao;
	
	@Column(name = "CONF_CD_USU_TRANSP_ABERT", length = 9)
	private Integer responsavelTransporteAbertura;	
	
	@Column(name = "CONF_CD_USU_TRANSP_FECH", length = 9)
	private Integer responsavelTransporteFechamento;

	@Column(name = "CONF_CD_LIB_AUD")
	private Integer crachaResponsavelLiberacaoAuditoria;
	
	@Column(name = "CONF_DH_LIB_AUD")
	private Date dataLiberacaoAuditoria;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CONF_SQ_SEQUENCIAL")
	private List<PreNfTransportada> preNfTransportada;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="conferencia")
	private List<ConferenciaOffline> conferenciaOffline;

	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="conferencia")
	private List<ObjetoConferencia> objetoConferencia;
	
}
