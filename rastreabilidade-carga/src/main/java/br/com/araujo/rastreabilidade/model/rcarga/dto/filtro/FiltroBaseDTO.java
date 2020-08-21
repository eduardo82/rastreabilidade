package br.com.araujo.rastreabilidade.model.rcarga.dto.filtro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroBaseDTO {

	protected Integer codigoFilial;
	protected String descricaoFilial;
	protected String dataInicial;
	protected String dataFinal;
	protected Integer rota;
}
