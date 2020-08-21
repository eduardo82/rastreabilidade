package br.com.araujo.rastreabilidade.model.rcarga;

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
@Table(name="AUDITORIA_UNITIZADOR_DET")
@Access(AccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DetalheAuditoria extends BaseAuditoriaRCarga {

	private static final long serialVersionUID = 2382479763729235421L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name = "DAUD_SQ_SEQUENCIAL", nullable=false, columnDefinition = "NUMERIC(10,0)")
	private Integer id;

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "AUDI_SQ_SEQUENCIAL", nullable=true)
	private Auditoria auditoria;	
	
    @Column (name = "PRME_CD_PRODUTO", length=5, columnDefinition = "NUMERIC(10,0)")
	private Integer codigoProduto;
    
    @Column (name = "DAUD_TX_LOTE", length=15)
	private String codigoLote;
    
    @Column (name = "DAUD_QT_UNIT_REC", length=5)
	private Integer quantidadeUnitariaRecebida;
    
    @Column (name = "DAUD_QT_UNIT_AVARIADA", length=5)
	private Integer quantidadeUnitariaAvariada;
    
    @Column (name = "DAUD_FL_LOTE_DIVERGENTE", columnDefinition = "char")
    private String loteDivergente;

}
