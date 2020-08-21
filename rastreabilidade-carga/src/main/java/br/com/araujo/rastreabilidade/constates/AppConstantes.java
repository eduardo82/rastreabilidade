package br.com.araujo.rastreabilidade.constates;

public interface AppConstantes {
	
	String STRING_VAZIA = "";

	interface FILTROS_PESQUISA {
		
		public static final String SITUACAO_FECHADO = "F";
		public static final String SITUACAO_EM_ABERTO = "E";
		public static final String SITUACAO_PARCIALMENTE_FECHADO = "P";
		public static final String SITUACAO_AMBOS = "A";
		
		public static final String TIPO_IMPRESSAO_IMPRESSO = "I";
		public static final String TIPO_IMPRESSAO_NAOIMPRESSO = "N";
		public static final String TIPO_IMPRESSAO_VERIFICACAO = "verificacao";
		
	}
	
    public static final String CONTROLE_BANCO = "COSMOS.DBO.";
	public static final char ST_CONF_ABERTA = 'A';
	public static final char ST_CONF_FINALIZADA = 'F';
	public static final char ST_CONF_CANCELADA = 'C';
	public static final char ST_CONF_PARCIALMENTE_FINALIZADA = 'P';
	
	public static final String CONFERENCIA_ABERTA = "Aberta";
	public static final String CONFERENCIA_FINALIZADA = "Fechada";
	public static final String CONFERENCIA_CANCELADA = "Cancelada";
	public static final String CONFERENCIA_PARCIALMENTE_FINALIZADA = "Fechada Parcialmente";
	public static final String CONFERENCIA_MANUAL = "Manual";
	
	//Constantes para o tipo de auditoria realizada
    public static final String TIPO_GERAL = "00";
    public static final String TIPO_PSICO = "01"; 
    public static final String TIPO_MED = "02";
    public static final String TIPO_PAR = "03";
    public static final String BEEPAR_CRACHA_RESP = "beepar_cracha_resp";
    
    //Constantes para o Tipo de Processo (Tipo da Ordem)
    public static final String TIPO_PROCESSO_PEDIDO_AUTOMATICO = "PEDIDO AUTOMÁTICO";
    public static final String TIPO_PROCESSO_PEDIDO_DIRETO = "PEDIDO DIRETO";
    
}
