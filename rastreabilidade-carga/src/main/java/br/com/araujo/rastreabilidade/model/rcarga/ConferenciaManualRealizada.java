package br.com.araujo.rastreabilidade.model.rcarga;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="CONFERENCIA_MANUAL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConferenciaManualRealizada extends BaseAuditoriaRCarga {

	private static final long serialVersionUID = -908163557868641999L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name = "CONM_SQ_SEQUENCIAL", nullable=false, columnDefinition = "NUMERIC(10,0)")
	private Integer id;

	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "PNFT_SQ_SEQUENCIAL", nullable=false)
	private PreNfTransportada preNfTransportada;
	
	@Column (name = "CONM_DH_INCLUSAO")
	private Date dataInclusao;
	
	@Column (name = "CONM_FL_EXCCARGA")
	private String flagExclusaoCarga;
	
	@Column (name = "CONM_DH_EXCCARGA")
	private Date dataExclusaoCarga;
}
