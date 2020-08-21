package br.com.araujo.rastreabilidade.model.rcarga.dto.filtro;

import java.util.List;

import br.com.araujo.rastreabilidade.constates.TipoElementoTela;
import br.com.araujo.rastreabilidade.model.rcarga.dto.comum.OpcaoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroTelaDTO {

	private TipoElementoTela tipo;	
	private String nome;
	private String descricao;
	private String requerido;
	private String valorPadrao;
	private String hint;
	private String customCss;
	private Integer tamanhoMaximo;
	private List<OpcaoDTO> opcoes;
}
