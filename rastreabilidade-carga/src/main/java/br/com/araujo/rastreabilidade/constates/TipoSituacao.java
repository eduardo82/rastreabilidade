package br.com.araujo.rastreabilidade.constates;

public enum TipoSituacao {
	
	EM_ABERTO("Em Aberto", "Aberto", "E"),
	FECHADO_PARCIALMENTE("Fechado Parcialmente", "Fechado Parcialmente", "P"),
	FECHADO("Fechado", "Fechado", "F"),
	AMBOS("Ambos", "Ambos", "A");

	private String descricao;
	private String valor;
	private String sigla;
	
	private TipoSituacao(String descricao, String valor, String sigla) {
		this.descricao = descricao;
		this.valor = valor;
		this.sigla = sigla;
	}
	
	public static TipoSituacao buscaPorSigla(String sigla) {
		for (TipoSituacao tipo : TipoSituacao.values()) {
			if (tipo.getSigla().equals(sigla)) {
				return tipo;
			}
		}
		
		return null;
	}
	
	public static String buscaValorPorSigla(String sigla) {
		for (TipoSituacao tipo : TipoSituacao.values()) {
			if (tipo.getSigla().equals(sigla)) {
				return tipo.getValor();
			}
		}
		
		return "";
	}
	
	public String getDescricao() {
		return descricao;
	}
	public String getSigla() {
		return sigla;
	}

	public String getValor() {
		return valor;
	}
}
