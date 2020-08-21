package br.com.araujo.rastreabilidade.model.rcarga.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.araujo.rastreabilidade.model.rcarga.Conferente;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConferenteDTO extends Conferente {

	private static final long serialVersionUID = 2639452757446071181L;

	public ConferenteDTO(String chapa, String nome) {
		setChapa(chapa);
		setNome(nome);
	}
	
	public ConferenteDTO(String chapa, String nome, Integer codigoFilial) {
		setChapa(chapa);
		setNome(nome);
		setCodigoFilial(codigoFilial);
	}
}
