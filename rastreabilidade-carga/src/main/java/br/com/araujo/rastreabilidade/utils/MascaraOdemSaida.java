package br.com.araujo.rastreabilidade.utils;

import java.math.BigDecimal;

public class MascaraOdemSaida {

	private MascaraOdemSaida() {}
	
	public static String ordemSaidaMask(Integer idFilial, BigDecimal dataJuliana, BigDecimal seqDataJuliana, String tipoProcesso){
		
		String maskFilial = "000" + idFilial.toString();
    	String maskDataJuliana = "0000000" + dataJuliana.toString();
    	String maskSeqDataJuliana = "0000000" + seqDataJuliana.toString();
    	
    	return maskFilial.substring(maskFilial.length() - 3, maskFilial.length()) + "-"
    			+ maskDataJuliana.substring(maskDataJuliana.length() - 7, maskDataJuliana.length())
    			+ maskSeqDataJuliana.substring(maskSeqDataJuliana.length() - 7, maskSeqDataJuliana.length())
    			+tipoProcesso;
	}
}
