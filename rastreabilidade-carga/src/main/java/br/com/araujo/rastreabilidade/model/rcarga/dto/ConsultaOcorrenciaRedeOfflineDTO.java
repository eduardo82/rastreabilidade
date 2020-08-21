package br.com.araujo.rastreabilidade.model.rcarga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultaOcorrenciaRedeOfflineDTO {

	private String dataEvento;
	private String horaEvento;
	private Integer codigoFilial;
	private String descricaoFilial;
	private String evento;
}
