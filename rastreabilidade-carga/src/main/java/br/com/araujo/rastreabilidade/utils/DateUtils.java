package br.com.araujo.rastreabilidade.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.ibm.icu.util.Calendar;

public class DateUtils {

	public static final String FORMATO_DATA = "dd/MM/yyyy";
	public static final String FORMATO_DATA_WEB = "yyyy-MM-dd";
	public static final String FORMATO_COMPLETO_BANCO = "dd/MM/yyyy hh:mm:ss";
	public static final String FORMATO_FINAL_DIA = "dd/MM/yyyy 23:59:59";
	
	public static final String FORMATO_FINAL_DIA_WEB = "yyyy-MM-dd 23:59:59";
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATO_DATA);
	
	private DateUtils() {}
	
	public static String formatDiaAtual() {
		return dateFormat.format(new Date());
	}
	
	public static String formatDiaAtualFormatoWeb() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATO_DATA_WEB);
		return dateFormat.format(new Date());
	}
	
	public static Date parseOrNull(String data) {
		try {
			if (StringUtils.isNotBlank(data)) {			
				return dateFormat.parse(data);
			}
			return null;
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date parseFinalDiaOrNull(String data, String pattern) {
		try {
			if (StringUtils.isNotBlank(data)) {	
				SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
				Date dataFinal = dateFormat.parse(data);
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dataFinal);
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				
				return calendar.getTime();
			}
			return null;
		} catch (ParseException e) {
			return null;
		}
	}
	
	
	
	public static Date parseOrNull(String data, String pattern) {
		try {			
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			return dateFormat.parse(data);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String format(Date data) {
		if (data != null) {			
			return dateFormat.format(data);
		}
		return "";
	}
	
	public static String format(Date data, String pattern) {
		if (data != null && StringUtils.isNotBlank(pattern)) {			
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			return dateFormat.format(data);
		}
		return "";
	}
}
