package br.com.araujo.rastreabilidade.model.rcarga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreNfTransportadaDTO {

	private Integer id;
	private String dataExportacao;
	private String nomeUsuario;
}
