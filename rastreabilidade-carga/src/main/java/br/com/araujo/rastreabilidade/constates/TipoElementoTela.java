package br.com.araujo.rastreabilidade.constates;

public enum TipoElementoTela {

	INPUT(1, "input"),
	DATE(2, "input type date"),
	SELECT(3, "select"),
	CHECKBOX(4, "input type checkbox"),
	INPUT_COMPOSTO(5, "input composto");
	
	private Integer codigo;
	private String descricao;
	
	private TipoElementoTela(Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
}
