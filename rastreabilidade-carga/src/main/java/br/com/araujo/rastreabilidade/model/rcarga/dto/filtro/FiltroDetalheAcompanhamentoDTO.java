package br.com.araujo.rastreabilidade.model.rcarga.dto.filtro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroDetalheAcompanhamentoDTO {
	private String rota;
	private String codFilial;
	private String nomeFilial;
	private String ordemSaida;
	private String prenota;
	private String conferencia;
	private String tipo;
}
