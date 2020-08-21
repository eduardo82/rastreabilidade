package br.com.araujo.rastreabilidade.model.rcarga;

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
@Table(name="UNITIZADOR_TRANSPORTADO")
@Access(AccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UnitizadorTransportado extends BaseAuditoriaRCarga {
	
	private static final long serialVersionUID = 3739911769428691514L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name = "UNTP_SQ_SEQUENCIAL", nullable=false, columnDefinition = "NUMERIC(10,0)")
	private Integer id;

	@OneToMany (fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="unitizadorTransportado")
	@Valid
	private List<ProdutoTransportado> produtoTransportado;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "PNFT_SQ_SEQUENCIAL", nullable=true)
	private PreNfTransportada preNfTransportada;
	
	@OneToMany (fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="unitizadorTransportado")
	@Valid
	private List<ObjetoConferencia> objetoConferencia;	
	
	@Column (name = "UNTP_NR_UNITIZADOR", length=44, columnDefinition = "char")
	private String numeroUnitizador;
	
	@Column (name = "UNTP_NR_LACRE_1", length=11, columnDefinition = "char")
	private String numeroLacre1;
	
	@Column (name = "UNTP_NR_LACRE_2", length=11, columnDefinition = "char")
	private String numeroLacre2;	
	
	@Column (name = "UNTP_TP_UNITIZADOR", length=2, columnDefinition = "char")
	private String tipoUnitizador;		

	@Column (name = "UNTP_TP_AUDITORIA", length=2, columnDefinition = "char")
	private String tipoAuditoria;		
	
	@Column (name = "UNTP_FL_OBRIG_RESP", nullable=false, columnDefinition = "char")
	private String flagObrigResponsavel;	
}
