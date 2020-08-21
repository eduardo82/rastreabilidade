package br.com.araujo.rastreabilidade.model.cosmos.chave;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Access(AccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RotaKey implements Serializable {
	
	private static final long serialVersionUID = -6024708411825233310L;

	@Column (name = "ROTA_SQ_SEQUENCIAL", nullable=false)
	private Integer rotaSqSequencial;

	@Column (name = "ROTA_DT_ROTAENTREGA", nullable=false)
	private Date dataRotaEntrega;
}
