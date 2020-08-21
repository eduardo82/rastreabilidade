package br.com.araujo.rastreabilidade.model.cosmos;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.araujo.rastreabilidade.model.cosmos.chave.RotaKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ROTA")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rota {

	@EmbeddedId
	private RotaKey idNatural;
	
	@ManyToOne(targetEntity = RotaMotorista.class, fetch = FetchType.EAGER)
	@JoinColumn(name="ROMO_CD_MOTORISTA",nullable=false,insertable=false,updatable=false,referencedColumnName="ROMO_CD_MOTORISTA")
	private RotaMotorista rotaMotorista;
}
