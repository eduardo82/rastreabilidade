package br.com.araujo.rastreabilidade.model.rcarga.dto.filtro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroAcompanhamentoCargaDTO {

	private String dataInicial;
	private String dataFinal;
	private String situacaoCarga;
	private String rota;
	private String codigoFilial;
	private String ordemSaida;
	private String preNota;
	private String entregaCarga; 
	private String tipoUZ; 
	private String conferencia;
	private String farolComPresencaTransportador; 
	private String farolSemPresencaTransportador; 
	private String ordenacao;
}