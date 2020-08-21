package br.com.araujo.rastreabilidade.model.rcarga.dto.filtro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroEntregaNaoRealizadaDTO {

	private String dataRota;
	private Integer rota;
}
