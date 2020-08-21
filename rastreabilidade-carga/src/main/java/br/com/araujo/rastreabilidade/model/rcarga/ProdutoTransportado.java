package br.com.araujo.rastreabilidade.model.rcarga;

import java.math.BigDecimal;
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
import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRODUTO_TRANSPORTADO")
@Access(AccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProdutoTransportado extends BaseAuditoriaRCarga {

	private static final long serialVersionUID = 2049648412641229185L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name = "PDTP_SQ_SEQUENCIAL", nullable=false, columnDefinition = "NUMERIC(10,0)")
	private Integer id;
	
	@Column (name = "PRME_CD_PRODUTO", columnDefinition = "NUMERIC(10,0)")
	private Integer produto;
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "UNTP_SQ_SEQUENCIAL", nullable=false)
	private UnitizadorTransportado unitizadorTransportado;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PNFT_SQ_SEQUENCIAL")
	private PreNfTransportada preNotaFiscalTransportada;		
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="produtoTransportado")
	@Valid
	private List<ObjetoConferencia> objetoConferencia;	
	
	@Column (name = "ITNF_DT_JULARM")
	private BigDecimal dataJuliana;
	
	@Column (name = "ITNF_SQ_DTJULARM")
	private BigDecimal sequencialDataJuliana;
	
	@Column (name = "ITNF_TP_PROCESSO", length=2, columnDefinition = "char")
	private String tipoProcesso;
	
	@Column (name = "PDTP_TX_LOTE")
	private String codigoLote;
	
	@Column (name = "PDTP_QT_UNIT_EXPED")
	private Integer quantidadeExpedida;
	
	@Column (name = "PDTP_SG_EMBEXP", length=10)
	private String siglaEmbalagem;
	
	@Column (name = "PDTP_QT_EMBEXP", length=5)
	private Integer quantidadeEmbalagem;
}
