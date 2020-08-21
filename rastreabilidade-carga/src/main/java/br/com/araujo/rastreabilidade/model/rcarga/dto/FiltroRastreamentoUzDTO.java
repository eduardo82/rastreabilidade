package br.com.araujo.rastreabilidade.model.rcarga.dto;

import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FiltroRastreamentoUzDTO extends FiltroBaseDTO {

	private String codigoUnitizador;
}
