package br.com.araujo.rastreabilidade.constates;

import lombok.Getter;

public enum DetalheAcompanhamentoCargaEnum {
	
	UZ_EXPEDIDO("1", Constants.JOIN_EXPEDIDO, Constants.WHERE_EXPEDIDO),
	UZ_TIPO_UNITIZADOR_EXPEDIDO("UZ_UNITIZADOR_EXPEDIDO", Constants.JOIN_EXPEDIDO, Constants.WHERE_EXPEDIDO + Constants.AND_TIPO_UNITIZADOR),
	
	UZ_RECEBIDO("2", Constants.JOIN_COMUM, Constants.WHERE_COMUM),
	UZ_TIPO_UNITIZADOR_RECEBIDO("UZ_UNITIZADOR_RECEBIDO", Constants.JOIN_COMUM, Constants.WHERE_COMUM + Constants.AND_TIPO_UNITIZADOR),
	
	UZ_AVARIADO("3", Constants.JOIN_COMUM, Constants.WHERE_COMUM +
			"and (ocnf.statusUnitizadorVolumoso = 'A' OR ocnf.statusUnitizadorVolumoso = 'T') "),
	UZ_TIPO_UNITIZADOR_AVARIADO("UZ_UNITIZADOR_AVARIADO", Constants.JOIN_COMUM, Constants.WHERE_COMUM + Constants.AND_TIPO_UNITIZADOR +
			"and (ocnf.statusUnitizadorVolumoso = 'A' OR ocnf.statusUnitizadorVolumoso = 'T') "),
	
	UZ_FALTANTE("4", Constants.JOIN_FALTANTE, Constants.WHERE_FALTANTE),
	UZ_TIPO_UNITIZADOR_FALTANTE("UZ_UNITIZADOR_FALTANTE", Constants.JOIN_FALTANTE, Constants.WHERE_FALTANTE + Constants.AND_TIPO_UNITIZADOR),
	
	UZ_INDEVIDO("5", "", Constants.WHERE_INDEVIDO),
	AUDITORIA_REALIZADA("6", "", Constants.WHERE_AUDITORIA_REALIZADA),
	AUDITORIA_NAO_PLANEJADA("7", "", Constants.WHERE_AUDITORIA_NAO_REALIZADA);
		
	@Getter
	private String codigo;
	
	@Getter
	private String join;
	
	@Getter
	private String where;

	private DetalheAcompanhamentoCargaEnum(String codigo, String join, String where) {
		this.codigo = codigo;
		this.join = join;
		this.where = where;
	}
	
	public static DetalheAcompanhamentoCargaEnum getDetalhePorCodigo(String codigo) {
		for (DetalheAcompanhamentoCargaEnum detalhe :DetalheAcompanhamentoCargaEnum.values()) {
			if (detalhe.getCodigo().equals(codigo)) {
				return detalhe;
			}
		}
		
		return null;
	}
	
	public static String getClausulaJoin(String codigo) {
		for (DetalheAcompanhamentoCargaEnum detalhe :DetalheAcompanhamentoCargaEnum.values()) {
			if (detalhe.getCodigo().equals(codigo)) {
				return detalhe.getJoin();
			}
		}
		
		return "";
	}
	
	public static String getClausulaWhere(String codigo) {
		for (DetalheAcompanhamentoCargaEnum detalhe :DetalheAcompanhamentoCargaEnum.values()) {
			if (detalhe.getCodigo().equals(codigo)) {
				return detalhe.getWhere();
			}
		}
		
		return "";
	}

	/**
	 * Classe que auxiliará no builder da query.
	 * @author 9000248
	 *
	 */
	private static class Constants {
		public static String AND_TIPO_UNITIZADOR = " and untp.tipoUnitizador = :tipoUnitizador ";
		
		public static String JOIN_COMUM = " join untp.preNfTransportada pre "
					+ "join  untp.produtoTransportado pdtp "
					+ "join untp.objetoConferencia ocnf ";
		 
		public static String WHERE_COMUM = " where ocnf.conferencia.id = :conferencia "
					+ "and pre.id = :prenota "
					+ "and ocnf.produtoTransportado.id is null ";
		 
		public static String JOIN_EXPEDIDO = " join untp.produtoTransportado pdtp ";
		public static String WHERE_EXPEDIDO = " where untp.preNfTransportada.id = :prenota ";
		
		public static String JOIN_FALTANTE = " join  untp.produtoTransportado pdtp "
		  			+ " left outer join untp.objetoConferencia ocnf ";
		public static String WHERE_FALTANTE = " where untp.preNfTransportada.id = :prenota ";
		
		public static String WHERE_INDEVIDO = " where ocnf.codigoBarraObjetoIndevido is not null " +
				"   and ocnf.conferencia.id = :conferencia " +
				"   and (len(ocnf.codigoBarraObjetoIndevido ) = 8 or ocnf.codigoBarraObjetoIndevido like '%UZ%' or (len(ocnf.codigoBarraObjetoIndevido ) = 7 and  ocnf.codigoBarraObjetoIndevido like '[a-z]%') )" +
				" order by ocnf.codigoBarraObjetoIndevido";
		
		//------------------------AUDITORIA ------------------
		public static String WHERE_AUDITORIA_REALIZADA = " where ocnf.conferencia.id = :conferencia " +
				"   and pre.id = :prenota " +
				"   and ocnf.produtoTransportado.id is null " +
				"   and daud.codigoProduto = pdtp.produto ";
		
		public static String WHERE_AUDITORIA_NAO_REALIZADA =  " where ocnf.conferencia.id = :conferencia " +
		"   and pre.id = :prenota " +
		"   and ocnf.produtoTransportado.id is null " +
		"   and (untp.tipoAuditoria <> '00' or ocnf.statusUnitizadorVolumoso = 'A' or ocnf.statusLacre1 <> 'O') " +
		"   and audi.id is null " ;
	}
}
