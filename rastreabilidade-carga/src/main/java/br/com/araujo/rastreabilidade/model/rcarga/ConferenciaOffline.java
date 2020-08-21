package br.com.araujo.rastreabilidade.model.rcarga;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
@Table(name="CONFERENCIA_OFFLINE")
@Access(AccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConferenciaOffline extends BaseAuditoriaRCarga {

	private static final long serialVersionUID = -928755293061450651L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name = "CNOF_SQ_SEQUENCIAL", nullable=false, columnDefinition = "NUMERIC(10,0)")
	private Integer id;

	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "CONF_SQ_SEQUENCIAL", nullable=false)
	private ConferenciaCarga conferencia;	
	
	@Column (name = "CNOF_TX_OBJETO", length=20, columnDefinition = "char")
	private String objeto;
	
	@Column (name = "CNOF_TX_LACRE_1", length=20, columnDefinition = "char")
	private String lacre1;
	
	@Column (name = "CNOF_TX_LACRE_2", length=20, columnDefinition = "char")
	private String lacre2;
	
	@Column (name = "CNOF_ST_OBJETO", columnDefinition = "char")
	private String statusObjeto;
	
	@Column (name = "CNOF_ST_LACRE_1", columnDefinition = "char")
	private String statusLacre1;
	
	@Column (name = "CNOF_ST_LACRE_2", columnDefinition = "char")
	private String statusLacre2;

	@Column (name = "CNOF_TP_OBJETO", columnDefinition = "char")
	private String tipoObjeto;
	
	@Column (name = "CNOF_TX_LOTE", columnDefinition = "char")
	private String lote;
	
	@Column (name = "CNOF_QT_ITENS", length=10, columnDefinition = "NUMERIC(10,0)")
	private Integer quantidadeItens;
	
	@Column (name = "CNOF_DH_ALT")
	private Date dataUltAlteracao;
	
	@Column (name = "CNOF_NM_ALT", length=20, columnDefinition = "char")
	private String usuarioUltAlteracao;
}
