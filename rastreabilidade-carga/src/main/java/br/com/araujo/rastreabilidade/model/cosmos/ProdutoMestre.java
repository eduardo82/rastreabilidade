package br.com.araujo.rastreabilidade.model.cosmos;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "PRODUTO_MESTRE")
@Access(AccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoMestre {

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)		
	@Column (name = "PRME_CD_PRODUTO", nullable=false, columnDefinition = "NUMERIC(10,0)")
	private Integer codigoProduto;
	
	@Column (name = "EMBA_SG_EXPEDICAO", nullable=false, length=2)
	private String embalagemExpedicao;
	
	@Column (name = "PRME_TX_DESCRICAO1", nullable=false, length=35)
	private String descricao1;
	
	@Column (name = "PRME_NR_DV", nullable=false, columnDefinition = "NUMERIC(2,0)")
	private Integer dvCodigoProduto; 
}
