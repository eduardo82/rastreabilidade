package br.com.araujo.rastreabilidade.model.rcarga.dto.filtro;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroConferenciaManualRealizadaDTO {

	private String dataInicial;
	private String dataFinal;
	private String codigoFilial;
	private String conferenciaRealizada;
}
