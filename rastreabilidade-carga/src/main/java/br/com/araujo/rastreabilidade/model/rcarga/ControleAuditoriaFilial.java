package br.com.araujo.rastreabilidade.model.rcarga;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.araujo.rastreabilidade.constates.TipoSimNao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CONTROLE_AUDIT_FILIAL")
@Access(AccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ControleAuditoriaFilial extends BaseAuditoriaRCarga {

	private static final long serialVersionUID = 4930776978505594590L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name = "CTAF_SQ_SEQUENCIAL", nullable=false)
	private Integer id;
	
	@Column (name = "FILI_CD_FILIAL", nullable=true)
	private Integer filial;
	
	@Column (name = "CTAF_DH_INICIO_RC", nullable=true )	
	private Date dataInicioRC;
	
	@Column (name = "CTAF_TX_PRAZO_MAX", nullable=true )
	private Integer prazoMaximoRealizacaoAuditoria;
	
	@Enumerated(EnumType.STRING)
	@Column (name = "CTAF_FL_PARMAIS", nullable=false , columnDefinition = "char")
	private TipoSimNao flagParmais;
	
	@Enumerated(EnumType.STRING)
	@Column (name = "CTAF_FL_USU_PARMAIS", columnDefinition = "char")
	private TipoSimNao flagUsuarioRespAudParmais;
	
	@Enumerated(EnumType.STRING)
	@Column (name = "CTAF_FL_PSICO", columnDefinition = "char")
	private TipoSimNao flagPsicotropicos;
	
	@Enumerated(EnumType.STRING)
	@Column (name = "CTAF_FL_USU_PSICO", columnDefinition = "char")
	private TipoSimNao flagUsuarioRespAudPsicotropicos;
	
	@Enumerated(EnumType.STRING)
	@Column (name = "CTAF_FL_MEDESP", columnDefinition = "char")
	private TipoSimNao flagMedicamentosEspeciais;
	
	@Enumerated(EnumType.STRING)
	@Column (name = "CTAF_FL_USU_MEDESP", columnDefinition = "char")
	private TipoSimNao flagRespAudMedicamentosEspeciais;
	
	@Enumerated(EnumType.STRING)
	@Column (name = "CTAF_FL_ARQUIVOS", columnDefinition = "char")
	private TipoSimNao flagArquivos = TipoSimNao.N;
	
	@Column (name = "CTAF_TX_PRAZO_EXC" )
	private Integer prazoExclusaoArquivos;
}
