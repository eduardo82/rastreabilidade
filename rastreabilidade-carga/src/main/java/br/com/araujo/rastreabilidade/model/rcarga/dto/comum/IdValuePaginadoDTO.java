package br.com.araujo.rastreabilidade.model.rcarga.dto.comum;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdValuePaginadoDTO {

	private int draw;
	private int recordsTotal;
	private int recordsFiltered;
	private List<IdValueDTO> data;
}
