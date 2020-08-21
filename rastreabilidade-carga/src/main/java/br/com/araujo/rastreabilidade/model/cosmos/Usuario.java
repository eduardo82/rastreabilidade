package br.com.araujo.rastreabilidade.model.cosmos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

	@Id
	@Column (name = "USUA_CD_USUARIO", nullable=false)
	private Integer id;
	
	@Column (name = "USUA_NM_USUARIO", nullable=false)
	private String nome;
	
	@Column (name = "USUA_TX_MATRICULA", nullable=false)
	private String matricula;
	
	@Column (name = "USUA_NM_LOGIN_USU", nullable=false)
	private String login;
}
