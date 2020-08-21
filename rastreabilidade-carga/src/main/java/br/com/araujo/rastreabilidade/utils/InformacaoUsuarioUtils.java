package br.com.araujo.rastreabilidade.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InformacaoUsuarioUtils {

	private String codigoFilial;
	private String nomeFilial;
	private String nomeUsuario;
	private String matricula;
	private String ipMaquina;
	private String sessionId;
	private String displayName;
	private Boolean admin;
	private String email; 
	private String impressora;
}