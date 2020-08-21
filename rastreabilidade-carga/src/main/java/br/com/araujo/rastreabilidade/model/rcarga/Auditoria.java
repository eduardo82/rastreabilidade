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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="AUDITORIA_UNITIZADOR_CAB")
@Access(AccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Auditoria extends BaseAuditoriaRCarga {

	private static final long serialVersionUID = -6779647392827195727L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name = "AUDI_SQ_SEQUENCIAL", nullable=false, columnDefinition = "NUMERIC(10,0)")
	private Integer id;

	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "OCNF_SQ_SEQUENCIAL", nullable=true)
	private ObjetoConferencia objetoConferencia;
	
	@OneToMany (fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="auditoria")
	@Valid
	private List<DetalheAuditoria> detalheAuditoria;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name = "AUDI_DH_INICIO")
	private Date dataInicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column (name = "AUDI_DH_FIM")
	private Date dataFim;
	
	@Column (name = "AUDI_CD_USUARIO", length=5)
	private Integer cdUsuarioRespAuditoria;
	
	@Column (name = "AUDI_TP_AUDITORIA", nullable=true, length=2)
	private String tipoAuditoria;
	
	@Column (name = "AUDI_FL_OBEDECE_PRAZOAUDIT", nullable=true, columnDefinition = "char")
	private String obedecePrazoAuditoria ;

}
