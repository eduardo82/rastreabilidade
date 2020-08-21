package br.com.araujo.rastreabilidade.constates;

public enum TipoImpressao {
	
	IMPRESSO("Impresso", "I"),
	NAO_IMPRESSO("Não Impresso", "N"),
	REIMPRESSAO("Reimpressão", "R");

	private String descricao;
	private String sigla;
	
	
	private TipoImpressao(String descricao, String sigla) {
		this.descricao = descricao;
		this.sigla = sigla;
	}
	
	public static TipoImpressao buscaPorSigla(String sigla) {
		for (TipoImpressao tipo : TipoImpressao.values()) {
			if (tipo.getSigla().equals(sigla)) {
				return tipo;
			}
		}
		
		return TipoImpressao.REIMPRESSAO;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public String getSigla() {
		return sigla;
	}

	
	
}
