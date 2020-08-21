package br.com.araujo.rastreabilidade.model.rcarga.dto.comum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdValueDTO {
	
	private Integer id;
	private String descricao;
}
