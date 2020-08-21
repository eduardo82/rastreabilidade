package br.com.araujo.rastreabilidade.model.cosmos;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "ROTA_MOTORISTA")
@Access(AccessType.FIELD)
@Data
@EqualsAndHashCode(callSuper = false)
public class RotaMotorista extends BaseAuditoriaCosmos {

	private static final long serialVersionUID = -8362035172411397313L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name = "ROMO_CD_MOTORISTA", columnDefinition = "NUMERIC(10,0)")
	private Integer id;
	
	@Column (name = "ROMO_NM_MOTORISTA", nullable=false, length=50)
	private String nomeMotorista;
}
