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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;

import br.com.araujo.rastreabilidade.model.cosmos.Rota;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="OBJETO_CONFERENCIA_CARGA")
@Access(AccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ObjetoConferencia extends BaseAuditoriaRCarga {

	private static final long serialVersionUID = -8166017322361609440L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name = "OCNF_SQ_SEQUENCIAL", nullable=false, columnDefinition = "NUMERIC(10,0)")
	private Integer id;

	@OneToMany (fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="objetoConferencia")
	@Valid
	private List<Auditoria> auditoria;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "CONF_SQ_SEQUENCIAL")
	private ConferenciaCarga conferencia;
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "PDTP_SQ_SEQUENCIAL", nullable=true)
	private ProdutoTransportado produtoTransportado;
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "UNTP_SQ_SEQUENCIAL")
	private UnitizadorTransportado unitizadorTransportado;	
	
	@Column (name = "OCNF_ST_UNITIZADOR_VOLUMOSO", nullable=true, columnDefinition = "char")
	private String statusUnitizadorVolumoso;
	
	@Column (name = "OCNF_ST_LACRE1", columnDefinition = "char")
	private String statusLacre1;
	
	@Column (name = "OCNF_ST_LACRE2", columnDefinition = "char")
	private String statusLacre2;
	
	@Column (name = "OCNF_DH_CONFERENCIA")
	private Date dataConferencia;
	
	@Column (name = "OCNF_TX_LOTE", columnDefinition = "char")
	private String lote;	
	
	@Column (name = "OCNF_QT_UNIT_REC", length=5, columnDefinition = "NUMERIC(5,0)")
	private Integer quantidadeUnitariaRecebida;	
	
	@Column (name = "OCNF_QT_UNIT_AVARIADA", length=5)
	private Integer quantidadeUnitariaAvariada;
	
	@Column (name = "OCNF_QT_UNIT_INDEVIDOS", length=5)
	private Integer quantidadeUnitariaIndevidos;
	
	@Column (name = "OCNF_CD_BARRA_INDEVIDO", length=14, columnDefinition = "char")
	private String codigoBarraObjetoIndevido;
	
	@Transient
	private Rota rota;
	
	@Transient
	private Date dataRota;
}