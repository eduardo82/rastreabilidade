package br.com.araujo.rastreabilidade.model.rcarga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReciboTransportadorDTO extends ConferenteDTO {

	private static final long serialVersionUID = -916576509826871354L;

	private String responsavelLoja;
	private String transportadorResponsavel;
	private String flagTipo;
	private String flagStatus;
}
