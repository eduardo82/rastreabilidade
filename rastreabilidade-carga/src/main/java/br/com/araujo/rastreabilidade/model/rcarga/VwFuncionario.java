package br.com.araujo.rastreabilidade.model.rcarga;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "VW_FUNCIONARIOS")
@Access(AccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VwFuncionario {

	@Id
//	@GeneratedValue()
	@Column (name = "USUARIO", nullable=false ,length=11)
	private String usuario;
	
	@Column (name = "NOME", nullable=false , length= 40	)
	private String nome;
	
	@Column (name = "DESCFUNCAO", nullable=false)
	private String funcao;
	
	@Column (name = "DESCSECAO", nullable=false)
	private String secao;
	
	public VwFuncionario(String nome, String usuario) { 
		setNome(nome);
		setUsuario(usuario);
	}
}