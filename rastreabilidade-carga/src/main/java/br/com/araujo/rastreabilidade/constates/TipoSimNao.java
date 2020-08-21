package br.com.araujo.rastreabilidade.constates;

public enum TipoSimNao {
	
	S("Sim", "S", Boolean.TRUE),
	N("Não", "N", Boolean.FALSE);

	private String descricao;
	private String sigla;
	private Boolean valorBoleano;
	
	
	private TipoSimNao(String descricao, String sigla, Boolean valorBoleano) {
		this.descricao = descricao;
		this.sigla = sigla;
		this.valorBoleano = valorBoleano;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public String getSigla() {
		return sigla;
	}

	public Boolean getValorBoleano() {
		return valorBoleano;
	}
}
