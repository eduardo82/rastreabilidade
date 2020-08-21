package br.com.araujo.rastreabilidade.model.rcarga;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="CONFERENTE")
@Access(AccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Conferente extends BaseAuditoriaRCarga {

	private static final long serialVersionUID = -9169142670946005333L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "COTE_SQ_SEQUENCIAL", nullable=false)
	private Integer id;
	
	@Column(name = "COTE_NOME", nullable=true, length=40 )
	private String nome;
		
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CONE_DH_INCLUSAO", nullable=false )
	private Date dataInclusao ;
	
	@Column(name = "FILI_CD_FILIAL", nullable=false)
	private Integer codigoFilial;
	
	@Column(name = "CONE_FECHAMENTO_FINAL")
	private String conferenteFinal;
	
	@Column(name = "CONE_AUD_MED_ESPECIAIS")
	private String audMedicaEspecias;
		
	@Column(name = "CONE_AUD_PARAMAIS")
	private String audParMais;
	
	@Column(name = "CONE_AUD_CONTROLADOS")
	private String audControlados; 
	
	@Column(name = "COTE_CHAPA_CONFERENTE", nullable=true , length=11)
	private String chapa;
}
