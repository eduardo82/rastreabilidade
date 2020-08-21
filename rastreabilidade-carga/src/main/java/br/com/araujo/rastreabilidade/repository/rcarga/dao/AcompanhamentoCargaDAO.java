package br.com.araujo.rastreabilidade.repository.rcarga.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.araujo.rastreabilidade.constates.AppConstantes;
import br.com.araujo.rastreabilidade.model.rcarga.dto.AcompanhamentoCargaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroAcompanhamentoCargaDTO;

@Repository
public class AcompanhamentoCargaDAO {

	@Autowired
	private DataSource dataSource;
	
	/**
	 * Recupera as listas para a tela de seleção do caso de uso Acompanhamento de Carga UC015
	 * 
	 * Recebe como parâmetros os campos da tela de argumento (filtros) do mesmo caso de uso.
	 * 
	 */
	public List<AcompanhamentoCargaDTO> buscaAcompanhamentoCarga(FiltroAcompanhamentoCargaDTO filtroDTO) throws Exception {
		try {
			
			StringBuilder sql = new StringBuilder();
			StringBuilder where = new StringBuilder();
			StringBuilder whereTabelaExterna = new StringBuilder();
			StringBuilder filtroPorTipoUZ = new StringBuilder(""); //Usado para agrupar os subselects do UZ quando utilizado o "filtro UZ"
			
			// ** INÍCIO DOS FILTROS DA TELA DE ARGUMENTO DO CASO DE USO ACOMPANHAMENTO DE CARGA ** 
			
			//Filtro Nº Conferência
			if (StringUtils.isNotBlank(filtroDTO.getConferencia()))
				where.append(" AND CONF.CONF_SQ_SEQUENCIAL = " + filtroDTO.getConferencia());
			
			//Filtro Rota
			if (StringUtils.isNotBlank(filtroDTO.getRota()))
				where.append(" AND PNFT.ROTA_SQ_SEQUENCIAL = " + filtroDTO.getRota());
			
			//Filtro Loja (filial) 
			if (StringUtils.isNotBlank(filtroDTO.getCodigoFilial()))
				where.append(" AND PNFT.FILI_CD_FILIAL = " + filtroDTO.getCodigoFilial());
			
			//Filtro Pré-Nota
			if (StringUtils.isNotBlank(filtroDTO.getPreNota()))
				where.append(" AND PNFT.PNFC_SQ_SEQUENCIAL = " + filtroDTO.getPreNota());
			
			//Filtro Data Expedição Inicial /  Data Expedição Final
			if (StringUtils.isNotBlank(filtroDTO.getDataInicial()) && StringUtils.isNotBlank(filtroDTO.getDataFinal()))
				where.append(" AND PNFT.ROTA_DT_ROTAENTREGA BETWEEN '"+filtroDTO.getDataInicial()+"' AND '"+filtroDTO.getDataFinal()+"' ");
			
					
			if (StringUtils.isNotBlank(filtroDTO.getSituacaoCarga())) {

				//Filtro Situação (combo estático) - Expedido
				if ("E".equals(filtroDTO.getSituacaoCarga())) {					
					where.append(" AND CONF.CONF_SQ_SEQUENCIAL IS NULL");
				}
				
				//Filtro Situação (combo estático) - Conferência Aberta
				if ("CA".equals(filtroDTO.getSituacaoCarga())) {					
					where.append(" AND CONF.CONF_DH_ABERTURA IS NOT NULL " +
							"AND CONF.CONF_DH_FECHAMENTO IS NULL ");
				}
				
				//Filtro Situação (combo estático) - Conferência Parcialmente Fechada
				if ("CP".equals(filtroDTO.getSituacaoCarga())) {					
					where.append(" AND CONF.CONF_DH_ABERTURA IS NOT NULL " +
							"AND CONF_DH_FECHAMENTO_PARCIAL IS NOT NULL ");
				}
				
				//Filtro Situação (combo estático) - Conferência fechada
				if ("CF".equals(filtroDTO.getSituacaoCarga())) {					
					where.append(" AND CONF.CONF_DH_ABERTURA IS NOT NULL " +
							" AND CONF.CONF_DH_FECHAMENTO IS NOT NULL ");
				}
			}
			
			//INICIO MODIFICAÇÔES VINICIUS MOTA 
			//enumeration: TipoUz.java
			//Tipo Unitizador - AppConstantesComuns.java
			//	Verifica se escolheu algum Controlado/PAR+/MED. ESPECIAIS/GERAL
			if(StringUtils.isNotBlank(filtroDTO.getTipoUZ()) && !"04".equals(filtroDTO.getTipoUZ())) {
				where.append(" AND (UNTP.UNTP_TP_UNITIZADOR = '");
				where.append(filtroDTO.getTipoUZ());
				where.append("')");
				
				//Define o filtro para ser usado nos subselects do UZ
				filtroPorTipoUZ.append(" AND ( UNTPS.UNTP_TP_UNITIZADOR = '").append(filtroDTO.getTipoUZ()).append("') ");
			}

			
			if (StringUtils.isNotBlank(filtroDTO.getEntregaCarga())) {
				//Filtro Situação Entrega (combo estático) - Conforme
				if ("C".equals(filtroDTO.getEntregaCarga())) {					
					whereTabelaExterna.append(" WHERE (QTD_UZ_EXP = QTD_UZ_REC) AND (QTD_UZ_AVA = 0) AND (QTD_VOL_EXP = " +
							"QTD_VOL_REC) AND (QTD_VOL_AVA = 0)  AND (QTD_VOL_IND = 0)  ");
				}

				//Filtro Situação Entrega (combo estático) -  Não conforme
				if ("N".equals(filtroDTO.getEntregaCarga())) {					
					whereTabelaExterna.append("WHERE (QTD_UZ_EXP <> QTD_UZ_REC) OR (QTD_UZ_AVA > 0) " +
							" OR ((QTD_UZ_EXP - QTD_UZ_REC) > 0) OR (QTD_UZ_IDV > 0) OR (QTD_VOL_EXP = QTD_VOL_REC) " +
							" OR (QTD_VOL_AVA > 0) OR ((QTD_VOL_EXP - QTD_VOL_REC) > 0) OR (QTD_VOL_IND > 0) ");
				}
				
			}
			
            //Filtro Ordem de Saída (pedido)
			if (StringUtils.isNotBlank(filtroDTO.getOrdemSaida())) {
				
				if(StringUtils.isBlank(filtroDTO.getEntregaCarga()) || "A".equals(filtroDTO.getEntregaCarga()))
					whereTabelaExterna.append(" WHERE PEDIDO = '" + filtroDTO.getOrdemSaida() +"' ");
				else
					whereTabelaExterna.append(" AND PEDIDO = '" + filtroDTO.getOrdemSaida() + "' ");
			}
			
            // ** FIM DOS FILTROS DA TELA DE ARGUMENTO DO CASO DE USO ACOMPANHAMENTO DE CARGA ** 
			
			//INICIO SELECT 
			sql.append("SELECT * FROM( "+
					"SELECT " +
					"	COUNT(DISTINCT UNTP.UNTP_SQ_SEQUENCIAL) AS QTD_UZ_EXP, " +
					
					//Alterações Vinícius Mota
					/**
					 * Conta UZ Recebido, Foi necessário um subselect para evitar problema de agrupamentos
					 * Quando utilizado filtro por tipo de UZ.
					 */
					"	(SELECT COUNT(1) AS EXPR1 " +
					"      FROM OBJETO_CONFERENCIA_CARGA AS OCNFS (NOLOCK)  " +
					"          INNER JOIN UNITIZADOR_TRANSPORTADO AS UNTPS (NOLOCK) ON 	(OCNFS.UNTP_SQ_SEQUENCIAL = UNTPS.UNTP_SQ_SEQUENCIAL) " +
					"		WHERE (CONF_SQ_SEQUENCIAL = CONF.CONF_SQ_SEQUENCIAL) AND UNTPS.PNFT_SQ_SEQUENCIAL = PNFT.PNFT_SQ_SEQUENCIAL  " +
					filtroPorTipoUZ.toString() +
					") AS QTD_UZ_REC, " +					
					//"	COUNT(DISTINCT OCNF.UNTP_SQ_SEQUENCIAL) AS QTD_UZ_REC, " +  //Antiga contagem
					
					"	ITNF_DT_JULARM, " +
					"	ITNF_SQ_DTJULARM, " +
					"	ITNF_TP_PROCESSO, " +
					"	PEDIDO, " +
					"   (convert(varchar,PNFT.ROTA_DT_ROTAENTREGA,103)) as ROTA_DT_ROTAENTREGA," +
					"	PNFT.FILI_CD_FILIAL, " +
					"	(SELECT FILI.FILI_NM_FANTASIA " +
					"      FROM "+AppConstantes.CONTROLE_BANCO+"FILIAL FILI (NOLOCK) " +
					"	  WHERE FILI.FILI_CD_FILIAL = PNFT.FILI_CD_FILIAL)  AS FILI_NM_FANTASIA, " +
					"	PNFT.ROTA_SQ_SEQUENCIAL, " +
					"	ISNULL(ROFI.ROFI_SQ_FILIALROTA,0) AS ROFI_SQ_FILIALROTA, " +
					"	PNFT.PNFC_SQ_SEQUENCIAL, " +
					"	PNFT.PNFT_SQ_SEQUENCIAL, " +
					"	CONF.CONF_SQ_SEQUENCIAL, " +
					"   (SELECT CNFE.CONF_FL_STATUS " +
					"		FROM CONFERENCIA_CARGA CNFE (NOLOCK) " +
					"		WHERE (CNFE.CONF_SQ_SEQUENCIAL =CONF.CONF_SQ_SEQUENCIAL)) AS CONF_FL_STATUS, "+
					" (convert(varchar,CONF.CONF_DH_ABERTURA,103)  +' '+ " +
					" SUBSTRING(convert(varchar,CONF.CONF_DH_ABERTURA,108),1,5)) as CONF_DH_ABERTURA, "+
					" (convert(varchar,CONF.CONF_DH_FECHAMENTO_PARCIAL,103)  +' '+ " +
					" SUBSTRING(convert(varchar,CONF.CONF_DH_FECHAMENTO_PARCIAL,108),1,5)) as CONF_DH_FECHAMENTO_PARCIAL, " +
					
					" (convert(varchar,CONF.CONF_DH_FECHAMENTO,103)  +' '+ " +
					" SUBSTRING(convert(varchar,CONF.CONF_DH_FECHAMENTO,108),1,5)) as CONF_DH_FECHAMENTO, " +
					
					//Alterações Vinícius Mota
					//Conta UZs avariados
					"	(SELECT COUNT(1) AS EXPR1 " +
					"      FROM OBJETO_CONFERENCIA_CARGA AS OCNFS (NOLOCK)  " +
					"      INNER JOIN UNITIZADOR_TRANSPORTADO AS UNTPS (NOLOCK)	ON 	(OCNFS.UNTP_SQ_SEQUENCIAL = UNTPS.UNTP_SQ_SEQUENCIAL) " +
					"		WHERE (CONF_SQ_SEQUENCIAL = CONF.CONF_SQ_SEQUENCIAL) AND (OCNFS.OCNF_ST_UNITIZADOR_VOLUMOSO " +
					"       = 'A' OR OCNFS.OCNF_ST_UNITIZADOR_VOLUMOSO = 'T') " +
					"AND (OCNFS.UNTP_SQ_SEQUENCIAL = UNTPS.UNTP_SQ_SEQUENCIAL) AND UNTPS.PNFT_SQ_SEQUENCIAL = PNFT.PNFT_SQ_SEQUENCIAL " +

					filtroPorTipoUZ.toString() +
					") AS QTD_UZ_AVA, " +
										
					//Conta UZs Indevidos
					
				
					" ( " +
					sqlSubQueryQtdeUZIndevidos(filtroDTO.getTipoUZ()) +
			/*		"SELECT COUNT(1) AS EXPR1   " +
					"		FROM OBJETO_CONFERENCIA_CARGA AS OCNFS "+
					"		WHERE (OCNFS.CONF_SQ_SEQUENCIAL = CONF.CONF_SQ_SEQUENCIAL) " +
					"		AND (len(OCNFS.OCNF_CD_BARRA_INDEVIDO) = 8 OR OCNFS.OCNF_CD_BARRA_INDEVIDO LIKE 'UZ%')" +
				*/
					") 	AS QTD_UZ_IDV, " +
							
					//Conta UZs com auditoria planejada
					"			(SELECT COUNT(1) AS EXPR1 " +
					"			FROM PRE_NF_TRANSPORTADA AS PNFTS (NOLOCK) INNER JOIN " +
					"			UNITIZADOR_TRANSPORTADO AS UNTPS (NOLOCK)  ON PNFTS.PNFT_SQ_SEQUENCIAL = UNTPS.PNFT_SQ_SEQUENCIAL " +
					"			WHERE (PNFTS.CONF_SQ_SEQUENCIAL = CONF.CONF_SQ_SEQUENCIAL) AND (UNTPS.UNTP_TP_AUDITORIA" +
					"			 <> '00')" +
					filtroPorTipoUZ.toString() +
					") AS QTD_UZ_AUD_PLA, " +
					
					//Conta UZs com auditoria realizada
					"			(SELECT COUNT(1) AS EXPR1 " +
					"			FROM AUDITORIA_UNITIZADOR_CAB AS AUDIS (NOLOCK)  INNER JOIN " +
					"			OBJETO_CONFERENCIA_CARGA AS OCNFS (NOLOCK) ON AUDIS.OCNF_SQ_SEQUENCIAL = OCNFS.OCNF_SQ_SEQUENCIAL" +
					"			 INNER JOIN UNITIZADOR_TRANSPORTADO AS UNTPS (NOLOCK) ON (OCNFS.UNTP_SQ_SEQUENCIAL = UNTPS.UNTP_SQ_SEQUENCIAL) " +
					"			WHERE (OCNFS.CONF_SQ_SEQUENCIAL = CONF.CONF_SQ_SEQUENCIAL) AND UNTPS.PNFT_SQ_SEQUENCIAL = PNFT.PNFT_SQ_SEQUENCIAL " +
					 filtroPorTipoUZ.toString() +
					") AS QTD_UZ_AUD_REA, " +
					
					//Quantifica as auditorias não realizadas - somente planejadas
					// Alterado por Marco Quiçula em 12/Abril/2010
					// Passou a trazer apenas as auditorias não realizadas que foram planejadas
					// Quantidade total de auditorias não realizadas pode ser obtida somando-se o valor deste resultado
					// com o soma da quantidade de auditorias nao realizadas que não foram planejadas
					"   (select COUNT(1) AS EXPR1   " +
					"		from OBJETO_CONFERENCIA_CARGA AS OCNFS (NOLOCK) " +
					"		INNER JOIN UNITIZADOR_TRANSPORTADO AS UNTPS (NOLOCK) " +
					"		ON OCNFS.UNTP_SQ_SEQUENCIAL=UNTPS.UNTP_SQ_SEQUENCIAL " +
					"		LEFT JOIN AUDITORIA_UNITIZADOR_CAB AS AUNDS (NOLOCK)  " +
					"		ON  OCNFS.OCNF_SQ_SEQUENCIAL  = AUNDS.OCNF_SQ_SEQUENCIAL " +
					"		where "+
					"		(OCNFS.CONF_SQ_SEQUENCIAL = CONF.CONF_SQ_SEQUENCIAL) "+
					"		AND OCNFS.PDTP_SQ_SEQUENCIAL IS NULL " +
					"		AND UNTPS.UNTP_TP_AUDITORIA <> '00' " +
					"		AND AUNDS.AUDI_SQ_SEQUENCIAL IS NULL " +
					"      AND UNTPS.PNFT_SQ_SEQUENCIAL = PNFT.PNFT_SQ_SEQUENCIAL  " +
					 filtroPorTipoUZ.toString() + 
					"		) AS QTD_UZ_AUD_PLAN_NAO_REA, " +	 
					
					//FIM ALTERAÇÕES VINICIUS MOTA
					
					// Incluido por Marco Quiçula em 12/Abril/2010
					//Quantifica as auditorias não realizadas - somente não planejadas
					"   (select COUNT(1) AS EXPR1   " +
					"		from OBJETO_CONFERENCIA_CARGA AS OCNFS (NOLOCK) " +
					"		INNER JOIN UNITIZADOR_TRANSPORTADO AS UNTPS (NOLOCK)  " +
					"		ON OCNFS.UNTP_SQ_SEQUENCIAL=UNTPS.UNTP_SQ_SEQUENCIAL " +
					"		LEFT JOIN AUDITORIA_UNITIZADOR_CAB AS AUNDS (NOLOCK) " +
					"		ON  OCNFS.OCNF_SQ_SEQUENCIAL  = AUNDS.OCNF_SQ_SEQUENCIAL " +
					"		where "+
					"		(OCNFS.CONF_SQ_SEQUENCIAL = CONF.CONF_SQ_SEQUENCIAL) "+
					"		AND OCNFS.PDTP_SQ_SEQUENCIAL IS NULL " +
					"		AND UNTPS.UNTP_TP_AUDITORIA = '00' " +
					"       AND ( OCNFS.OCNF_ST_UNITIZADOR_VOLUMOSO = 'A' OR OCNFS.OCNF_ST_LACRE1 <> 'O' ) " +
					"		AND AUNDS.AUDI_SQ_SEQUENCIAL IS NULL " +
					"      AND UNTPS.PNFT_SQ_SEQUENCIAL = PNFT.PNFT_SQ_SEQUENCIAL  " +
					 filtroPorTipoUZ.toString() + 
					"		) AS QTD_UZ_AUD_NAO_PLAN_NAO_REA, " +
					
					//Quantidade de itens com divergência de auditoria
					"   ( " +
					sqlSubQueryQtdeAuditoriaItensDivergentes( filtroPorTipoUZ.toString() ).toString() +
					"   ) AS QTD_UZ_DIV_AUD, " + 
					
					//Quantidade de itens de auditoria realizada fora do prazo
					"	( " +
					sqlSubQueryQtdeAuditoriaItensRealizadaForaDoPrazo( filtroPorTipoUZ.toString() ).toString() +
					"   ) AS QTD_AUD_FORA_PRZ, " + 
					
					//Quantidade de registros de conferência manual da pré-nota (deve ser 0 ou 1)
					"	( " +
					sqlSubQueryQtdeItensConferencialManual( ).toString() +
					"   ) AS QTD_CONF_MANUAL, " +
					
					//INICIO ALTERACAO FABRICIO GOMES EM 12/02/2010
					//subselect para QTD_VOL_EXP
					"			(SELECT  sum(ISNULL(PDTP.PDTP_QT_UNIT_EXPED,0)) AS QTD_VOL_EXP " +
					"			FROM PRODUTO_TRANSPORTADO PDTP (NOLOCK) " +
					"			JOIN "+AppConstantes.CONTROLE_BANCO+"PRODUTO_MESTRE PRME (NOLOCK) ON PRME.PRME_CD_PRODUTO " +
					"       	= PDTP.PRME_CD_PRODUTO " +
					"			LEFT JOIN OBJETO_CONFERENCIA_CARGA OCNF (NOLOCK) ON PDTP.PDTP_SQ_SEQUENCIAL " +
					"           = OCNF.PDTP_SQ_SEQUENCIAL " +
					"			WHERE PDTP.PNFT_SQ_SEQUENCIAL = PNFT.PNFT_SQ_SEQUENCIAL and " +
					"				  PDTP.UNTP_SQ_SEQUENCIAL IS NULL AND " +
					"				  PDTP.PDTP_SQ_SEQUENCIAL IS  NOT NULL ) as QTD_VOL_EXP, " +
					
					//subselect para QTD_VOL_REC
					" 			(SELECT  sum(ISNULL(OCNF.OCNF_QT_UNIT_REC - OCNF.OCNF_QT_UNIT_INDEVIDOS - OCNF.OCNF_QT_UNIT_AVARIADA,0)) AS QTD_VOL_REC " +
					"			FROM PRODUTO_TRANSPORTADO PDTP (NOLOCK) " +
					"			JOIN "+AppConstantes.CONTROLE_BANCO+"PRODUTO_MESTRE PRME (NOLOCK) ON PRME.PRME_CD_PRODUTO " +
					"           = PDTP.PRME_CD_PRODUTO " +
					"			LEFT JOIN OBJETO_CONFERENCIA_CARGA OCNF (NOLOCK) ON PDTP.PDTP_SQ_SEQUENCIAL " +
					"           = OCNF.PDTP_SQ_SEQUENCIAL " +
					"			WHERE PDTP.PNFT_SQ_SEQUENCIAL = PNFT.PNFT_SQ_SEQUENCIAL and " +
					"				  PDTP.UNTP_SQ_SEQUENCIAL IS NULL AND " +
					"				  PDTP.PDTP_SQ_SEQUENCIAL IS  NOT NULL ) as QTD_VOL_REC, " +
 
					//subselect para QTD_VOL_AVA
					" 			(SELECT  sum(ISNULL(OCNF.OCNF_QT_UNIT_AVARIADA,0)) AS QTD_VOL_AVA " +
					"			FROM PRODUTO_TRANSPORTADO PDTP (NOLOCK) " +
					"			JOIN "+AppConstantes.CONTROLE_BANCO+"PRODUTO_MESTRE PRME (NOLOCK) ON PRME.PRME_CD_PRODUTO " +
					"           = PDTP.PRME_CD_PRODUTO " +
					"			LEFT JOIN OBJETO_CONFERENCIA_CARGA OCNF (NOLOCK) ON PDTP.PDTP_SQ_SEQUENCIAL " +
					"           = OCNF.PDTP_SQ_SEQUENCIAL " +
					"			WHERE PDTP.PNFT_SQ_SEQUENCIAL = PNFT.PNFT_SQ_SEQUENCIAL and " +
					"				  PDTP.UNTP_SQ_SEQUENCIAL IS NULL AND " +
					"				  PDTP.PDTP_SQ_SEQUENCIAL IS  NOT NULL ) as QTD_VOL_AVA," +

					//subselect para QTD_VOL_IND
					"			(SELECT  sum(ISNULL(OCNF.OCNF_QT_UNIT_INDEVIDOS,0)) AS QTD_VOL_IND " +
					"			FROM PRODUTO_TRANSPORTADO PDTP (NOLOCK) " +
					"			JOIN "+AppConstantes.CONTROLE_BANCO+"PRODUTO_MESTRE PRME (NOLOCK) ON PRME.PRME_CD_PRODUTO " +
					"           = PDTP.PRME_CD_PRODUTO " +
					"			LEFT JOIN OBJETO_CONFERENCIA_CARGA OCNF (NOLOCK) ON PDTP.PDTP_SQ_SEQUENCIAL = " +
					"           OCNF.PDTP_SQ_SEQUENCIAL " +
					"			WHERE PDTP.PNFT_SQ_SEQUENCIAL = PNFT.PNFT_SQ_SEQUENCIAL and " +
					"				  PDTP.UNTP_SQ_SEQUENCIAL IS NULL AND " +
					"				  PDTP.PDTP_SQ_SEQUENCIAL IS  NOT NULL ) as QTD_VOL_IND " +
					"	FROM		(SELECT DISTINCT PDTP.PNFT_SQ_SEQUENCIAL, " +
					"					PNFT.FILI_CD_FILIAL, " +
					"					PDTP.ITNF_DT_JULARM, " +
					"					PDTP.ITNF_SQ_DTJULARM, " +
					"					PDTP.ITNF_TP_PROCESSO, " +
					"					REPLICATE('0',(3 - LEN(CAST(PNFT.FILI_CD_FILIAL AS VARCHAR)))) " +
					"                   + CAST(PNFT.FILI_CD_FILIAL AS VARCHAR) " +
					"					+ '-' " +
					"					+ REPLICATE('0',(7 - LEN(CAST(PDTP.ITNF_DT_JULARM AS VARCHAR)))) " +
					"                   + CAST(PDTP.ITNF_DT_JULARM AS VARCHAR) " +
					"					+ REPLICATE('0',(7 - LEN(CAST(PDTP.ITNF_SQ_DTJULARM AS VARCHAR)))) " +
					"                   + CAST(PDTP.ITNF_SQ_DTJULARM AS VARCHAR) " +
					"					+ PDTP.ITNF_TP_PROCESSO AS PEDIDO " +
					"				FROM PRODUTO_TRANSPORTADO PDTP (NOLOCK) " +
					"					JOIN PRE_NF_TRANSPORTADA PNFT (NOLOCK) " +
					"						ON PNFT.PNFT_SQ_SEQUENCIAL = PDTP.PNFT_SQ_SEQUENCIAL) AS PDTP " +
					"	JOIN PRE_NF_TRANSPORTADA AS PNFT  (NOLOCK) ON PDTP.PNFT_SQ_SEQUENCIAL = PNFT.PNFT_SQ_SEQUENCIAL " +
					"	JOIN UNITIZADOR_TRANSPORTADO UNTP (NOLOCK) ON UNTP.PNFT_SQ_SEQUENCIAL=PNFT.PNFT_SQ_SEQUENCIAL " +
					"	LEFT JOIN CONFERENCIA_CARGA AS CONF (NOLOCK) ON CONF.CONF_SQ_SEQUENCIAL = PNFT.CONF_SQ_SEQUENCIAL 	 " +
					
					"	LEFT JOIN OBJETO_CONFERENCIA_CARGA OCNF (NOLOCK) ON OCNF.CONF_SQ_SEQUENCIAL=CONF.CONF_SQ_SEQUENCIAL " +
					"	LEFT JOIN "+AppConstantes.CONTROLE_BANCO+"ROTA_FILIAL ROFI (NOLOCK) ON ROFI.ROTA_DT_ROTAENTREGA " +
					"   = PNFT.ROTA_DT_ROTAENTREGA AND " +
					"											 ROFI.ROTA_SQ_SEQUENCIAL = PNFT.ROTA_SQ_SEQUENCIAL AND " +
					"											 ROFI.FILI_CD_FILIAL = PNFT.FILI_CD_FILIAL " +
					"	WHERE 0=0 " );
					sql.append(where);
					sql.append(
					"	GROUP BY ITNF_DT_JULARM," +
					"			ITNF_SQ_DTJULARM, " +
					"			ITNF_TP_PROCESSO, " +
					"			PEDIDO, " +
					"			PNFT.ROTA_DT_ROTAENTREGA, " +
					"			PNFT.FILI_CD_FILIAL, " +
					"			PNFT.ROTA_SQ_SEQUENCIAL, " +
					"			ROFI.ROFI_SQ_FILIALROTA, " +
					"			PNFT.PNFC_SQ_SEQUENCIAL, " +
					"			PNFT.PNFT_SQ_SEQUENCIAL, " +
					"			CONF.CONF_SQ_SEQUENCIAL, " +
					"			CONF.CONF_DH_ABERTURA, " +
					"           CONF.CONF_DH_FECHAMENTO_PARCIAL, " +
					"			CONF.CONF_DH_FECHAMENTO " +					
					" ) AS TABELA " +
					""); 
				
					sql.append(whereTabelaExterna);
					
					//Ordenacao da consulta SM 013
					if(StringUtils.isNotBlank(filtroDTO.getOrdenacao())){
						if("POR_LOJA".equals(filtroDTO.getOrdenacao())){
							sql.append(" ORDER BY FILI_CD_FILIAL, ");
						}
						else if("POR_ROTA".equals(filtroDTO.getOrdenacao())){
							sql.append(" ORDER BY ROTA_SQ_SEQUENCIAL, ");
						}
					}
					else{
						sql.append("ORDER BY ");
					}
					
					sql.append(	" FILI_NM_FANTASIA, PNFT_SQ_SEQUENCIAL, CONF_SQ_SEQUENCIAL ASC ");
			
			PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(sql.toString());
			ResultSet resultSet = preparedStatement.executeQuery();
			 
			List<AcompanhamentoCargaDTO> listaAcompanhamentoCarga = new ArrayList<AcompanhamentoCargaDTO>();
			
			while(resultSet.next()){
				AcompanhamentoCargaDTO acompanhamentoCarga = new AcompanhamentoCargaDTO();
				acompanhamentoCarga.setDataExpedicao(trataString(resultSet.getString("ROTA_DT_ROTAENTREGA")));
				acompanhamentoCarga.setFilialAcompanhamento(trataLong(resultSet.getLong("FILI_CD_FILIAL")));
				acompanhamentoCarga.setNomeFantasia(validaNomeFantasia(resultSet.getString("FILI_NM_FANTASIA")));
				acompanhamentoCarga.setRotaAcompanhamento(trataLong(resultSet.getLong("ROTA_SQ_SEQUENCIAL")));
				acompanhamentoCarga.setSequencialRotaAcompanhamento(trataLong(resultSet.getLong("ROFI_SQ_FILIALROTA")));
				acompanhamentoCarga.setPreNotaCab(trataLong(resultSet.getLong("PNFC_SQ_SEQUENCIAL")));
				acompanhamentoCarga.setPreNota(trataLong(resultSet.getLong("PNFT_SQ_SEQUENCIAL")));
				acompanhamentoCarga.setPedido(resultSet.getString("PEDIDO"));
				acompanhamentoCarga.setConferenciaAcompanhamento(trataLong(resultSet.getLong("CONF_SQ_SEQUENCIAL")));
				acompanhamentoCarga.setDataAbertura(trataString(resultSet.getString("CONF_DH_ABERTURA")));
				acompanhamentoCarga.setDataFechamento(trataString(resultSet.getString("CONF_DH_FECHAMENTO")));
				acompanhamentoCarga.setDataFechamentoParcial(trataString(resultSet.getString("CONF_DH_FECHAMENTO_PARCIAL")));
				acompanhamentoCarga.setQuantidadeUzExpedido(trataLong(resultSet.getLong("QTD_UZ_EXP")));
				acompanhamentoCarga.setQuantidadeUzRecebido(trataLong(resultSet.getLong("QTD_UZ_REC")));
				acompanhamentoCarga.setQuantidadeUzAvariado(trataLong(resultSet.getLong("QTD_UZ_AVA")));
				acompanhamentoCarga.setQuantidadeUzFaltantes(trataLong(acompanhamentoCarga.getQuantidadeUzExpedido() 
						- acompanhamentoCarga.getQuantidadeUzRecebido())); // SERA CAMPO CALCULADO NO CODIGO JAVA
				acompanhamentoCarga.setQuantidadeUzIndevidos(trataLong(resultSet.getLong("QTD_UZ_IDV")));
				acompanhamentoCarga.setQuantidadeAuditoriaPlanejada(trataLong(resultSet.getLong("QTD_UZ_AUD_PLA")));
				acompanhamentoCarga.setQuantidadeAuditoriaRealizada(trataLong(resultSet.getLong("QTD_UZ_AUD_REA")));
				acompanhamentoCarga.setQuantidadeAuditoriaPlanejadaNaoRealizada(trataLong(resultSet.getLong("QTD_UZ_AUD_PLAN_NAO_REA")));
				acompanhamentoCarga.setQuantidadeAuditoriaNaoPlanejadaNaoRealizada(trataLong(resultSet.getLong("QTD_UZ_AUD_NAO_PLAN_NAO_REA")));
				acompanhamentoCarga.setQuantidadeAuditoriaItensComDivergencia(trataLong(resultSet.getLong("QTD_UZ_DIV_AUD")));
				acompanhamentoCarga.setQuantidadeAuditoriaItensRealizadaForaDoPrazo(trataLong(resultSet.getLong("QTD_AUD_FORA_PRZ")));
				acompanhamentoCarga.setQuantidadeItensConferenciaManual(trataLong(resultSet.getLong("QTD_CONF_MANUAL")));
				acompanhamentoCarga.setQuantidadeVolumosoExpedido(trataLong(resultSet.getLong("QTD_VOL_EXP")));
				acompanhamentoCarga.setQuantidadeVolumosoRecebido(trataLong(resultSet.getLong("QTD_VOL_REC")));
				acompanhamentoCarga.setQuantidadeVolumosoAvariado(trataLong(resultSet.getLong("QTD_VOL_AVA")));
				acompanhamentoCarga.setQuantidadeVolumosoFaltante(trataLong(acompanhamentoCarga.getQuantidadeVolumosoExpedido() 
						- (acompanhamentoCarga.getQuantidadeVolumosoRecebido() 
								+ acompanhamentoCarga.getQuantidadeVolumosoAvariado())));
				acompanhamentoCarga.setQuantidadeVolumosoIndevido(trataLong(resultSet.getLong("QTD_VOL_IND"))); 
				acompanhamentoCarga.setStatusConferencia(trataString(validaStatusDaConferencia(resultSet
						.getString("CONF_FL_STATUS"), acompanhamentoCarga)));
				
				acompanhamentoCarga.setFarolNaPresenca(montaFarolNaPresenca(acompanhamentoCarga));
				acompanhamentoCarga.setFarolAposLiberacao(montaFarolAposLiberacao(acompanhamentoCarga));
				acompanhamentoCarga.setExisteFarol(StringUtils.isNotBlank(acompanhamentoCarga.getFarolAposLiberacao()));
				listaAcompanhamentoCarga.add(acompanhamentoCarga);
				
			}
			
			if(preparedStatement!=null)
				preparedStatement.close();
			
			if(resultSet!=null)
				resultSet.close();
			
			return listaAcompanhamentoCarga;
			
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return new ArrayList<AcompanhamentoCargaDTO>();
	}
	
	private String montaFarolAposLiberacao(AcompanhamentoCargaDTO acompanhamento) {
		if (StringUtils.isNotBlank(acompanhamento.getStatusConferencia())) {			
			if (acompanhamento.getQuantidadeItensConferenciaManual() > 0) {
				return "cinza";
			} else if (acompanhamento.getQuantidadeAuditoriaPlanejadaNaoRealizada() > 0 || acompanhamento.getQuantidadeAuditoriaItensComDivergencia() > 0) {
				return "vermelho";
			} else if (acompanhamento.getQuantidadeAuditoriaItensRealizadaForaDoPrazo() > 0) {
				return "amarelo";
			} else {
				return "verde";
			}
		} 
		return "";
	}

	private String montaFarolNaPresenca(AcompanhamentoCargaDTO acompanhamento) {
		if (StringUtils.isNotBlank(acompanhamento.getStatusConferencia())) {			
			if (acompanhamento.getQuantidadeItensConferenciaManual() > 0) {
				return "cinza";
			} else if (acompanhamento.getQuantidadeUzFaltantes() > 0 || acompanhamento.getQuantidadeVolumosoFaltante() > 0 || 
					acompanhamento.getQuantidadeAuditoriaItensComDivergencia() > 0) {
				return "vermelho";
			} else if (acompanhamento.getQuantidadeAuditoriaNaoPlanejadaNaoRealizada() > 0) {
				return "amarelo";
			} else {
				return "verde";
			}
		}
		return "";
	}

	private String trataString(String string) {
		return StringUtils.isNotBlank(string) ? string : "";
	}
	
	private Long trataLong(Long numero) {
		return numero != null ? numero : 0L;
	}

	/**
	 * Método criado para tratar o nome da filial 41 - MCDONALD'S. O caractere ' gera erro no javascript do caso de uso.
	 * OBS: Está sendo tratado nesta classe, pois a consulta e montagem da lista não estão no padrão jCompany.
	 * @param nomeFantasia
	 * @return nomeFantasia
	 * @author Diogo Bittencourt - 22/03/2010
	 */
	private String validaNomeFantasia(String nomeFantasia) {
		
		nomeFantasia = nomeFantasia != null ? nomeFantasia.replace("'", "") : "";
		
		return nomeFantasia;
	}
	
	/**
	 * Verifica se o tipo de UZ foi setado como filtro e caso tenha sido retorna essa consulta com valor 0.
	 * Tal verificação é necessária pois não é possível agrupar UZs indevidos por tipo de auditoria no  SQL principal
	 * @return SQL preparado  para a consulta
	 * @author vinicius mota  29/04
	 */
	private String sqlSubQueryQtdeUZIndevidos(String tipoUZ){
		String sqlUzindevido = 		
			"SELECT COUNT(1) AS EXPR1   " +
			"FROM OBJETO_CONFERENCIA_CARGA AS OCNFS (NOLOCK) "+
			"WHERE (OCNFS.CONF_SQ_SEQUENCIAL = CONF.CONF_SQ_SEQUENCIAL) " +
			"AND (len(OCNFS.OCNF_CD_BARRA_INDEVIDO) = 8 OR OCNFS.OCNF_CD_BARRA_INDEVIDO LIKE 'UZ%' OR (len(OCNFS.OCNF_CD_BARRA_INDEVIDO) = 7 AND OCNFS.OCNF_CD_BARRA_INDEVIDO LIKE '[a-z]%' ) ) ";
		
		//UZs indevidos sempre são considerados  do tipo Geral
		if ( !"".equals(tipoUZ)  && (AppConstantes.TIPO_PSICO.equals(tipoUZ)
			     || AppConstantes.TIPO_MED.equals(tipoUZ)
			     || AppConstantes.TIPO_PAR.equals(tipoUZ))) {
			sqlUzindevido = " 0 ";
		}
		
		return sqlUzindevido;
	}
	
	
	/**
	 * Faz a substituição do status da conferência de 'char' para 'String' para ser exibido na lista de seleção do caso 
	 * de uso. 
	 * OBS: Está sendo tratado nesta classe, pois a consulta e montagem da lista não estão no padrão jCompany.
	 * @param statusConferencia
	 * @return statusConferencia
	 * @author Diogo Bittencourt - 22/03/2010
	 */
	private String validaStatusDaConferencia(String statusConferencia, AcompanhamentoCargaDTO acompanhamento) {
		
		if(statusConferencia != null){
			if(statusConferencia.equals(String.valueOf(AppConstantes.ST_CONF_ABERTA)))
				statusConferencia = AppConstantes.CONFERENCIA_ABERTA;
			if(statusConferencia.equals(String.valueOf(AppConstantes.ST_CONF_FINALIZADA)))
				statusConferencia = AppConstantes.CONFERENCIA_FINALIZADA;
			if(statusConferencia.equals(String.valueOf(AppConstantes.ST_CONF_CANCELADA)))
				statusConferencia = AppConstantes.CONFERENCIA_CANCELADA;
			if(statusConferencia.equals(String.valueOf(AppConstantes.ST_CONF_PARCIALMENTE_FINALIZADA)))
				statusConferencia = AppConstantes.CONFERENCIA_PARCIALMENTE_FINALIZADA;
		} else if (acompanhamento.getQuantidadeItensConferenciaManual() > 0) {
			statusConferencia = AppConstantes.CONFERENCIA_MANUAL;
		}
		
		return statusConferencia;
	}
	
	/**
	 * Retorna contagem de Itens(registros) que possuem divergência na auditoria
	 * @param filtroPorTipoUZ
	 * @return
	 */
	private StringBuffer sqlSubQueryQtdeAuditoriaItensDivergentes( String filtroPorTipoUZ )
	{
		
		filtroPorTipoUZ = filtroPorTipoUZ.replace("UNTPS.", "UNTP_sq.");
		
		StringBuffer sqlSubQuery = new StringBuffer();
		
		sqlSubQuery.append( "SELECT ");
		sqlSubQuery.append( "	COUNT(1) AS QTD_UZ_DIV_AUD_sq ");
		sqlSubQuery.append( "FROM ( ");
		sqlSubQuery.append( "SELECT ");
		sqlSubQuery.append( "	UNTP_sq.UNTP_NR_UNITIZADOR 																		AS UNTP_NR_UNITIZADOR, ");
		sqlSubQuery.append( "	PRME_sq.PRME_CD_PRODUTO 																		AS PRME_CD_PRODUTO, ");
		sqlSubQuery.append( "	PRME_sq.PRME_NR_DV 																				AS PRME_NR_DV, ");
		sqlSubQuery.append( "	PRME_sq.PRME_TX_DESCRICAO1 																		AS PRME_TX_DESCRICAO1, ");
		sqlSubQuery.append( "	SUM(CASE WHEN PDTP_sq.PDTP_QT_UNIT_EXPED IS NULL THEN 0 ELSE PDTP_sq.PDTP_QT_UNIT_EXPED END) 	AS QTDE_EXPEDIDA, ");
		sqlSubQuery.append( "	CASE WHEN DAUD_sq.DAUD_QT_UNIT_REC IS NULL THEN 0 ELSE DAUD_sq.DAUD_QT_UNIT_REC END 			AS QTDE_RECEBIDA, ");
		sqlSubQuery.append( "	CASE WHEN DAUD_sq.DAUD_QT_UNIT_AVARIADA IS NULL THEN 0 ELSE DAUD_sq.DAUD_QT_UNIT_AVARIADA END 	AS QTDE_AVARIA, ");
		sqlSubQuery.append( "	CASE WHEN DAUD_sq.DAUD_TX_LOTE IS NULL THEN '' ELSE DAUD_sq.DAUD_TX_LOTE END 					AS LOTE_CONF, ");
		sqlSubQuery.append( "	CASE WHEN PDTP_sq.PDTP_TX_LOTE IS NULL THEN '' ELSE PDTP_sq.PDTP_TX_LOTE END 					AS LOTE_PDTP, ");
		sqlSubQuery.append( "	OCNF_sq.CONF_SQ_SEQUENCIAL AS CONFERENCIA ");
		sqlSubQuery.append( "	FROM " );
		sqlSubQuery.append( "	COSMOS.DBO.PRODUTO_MESTRE PRME_sq (NOLOCK) ");
		sqlSubQuery.append( "	INNER JOIN AUDITORIA_UNITIZADOR_DET  DAUD_sq (NOLOCK) ON DAUD_sq.PRME_CD_PRODUTO    = PRME_sq.PRME_CD_PRODUTO ");
		sqlSubQuery.append( "	INNER JOIN AUDITORIA_UNITIZADOR_CAB  AUDI_sq (NOLOCK) ON AUDI_sq.AUDI_SQ_SEQUENCIAL = DAUD_sq.AUDI_SQ_SEQUENCIAL ");
		sqlSubQuery.append( "	INNER JOIN OBJETO_CONFERENCIA_CARGA  OCNF_sq (NOLOCK) ON OCNF_sq.OCNF_SQ_SEQUENCIAL = AUDI_sq.OCNF_SQ_SEQUENCIAL ");
		sqlSubQuery.append( "	INNER JOIN UNITIZADOR_TRANSPORTADO   UNTP_sq (NOLOCK) ON UNTP_sq.UNTP_SQ_SEQUENCIAL = OCNF_sq.UNTP_SQ_SEQUENCIAL ");
		sqlSubQuery.append( "	LEFT OUTER JOIN PRODUTO_TRANSPORTADO PDTP_sq (NOLOCK) ON PDTP_sq.UNTP_SQ_SEQUENCIAL = UNTP_sq.UNTP_SQ_SEQUENCIAL ");
		sqlSubQuery.append( "	AND DAUD_sq.PRME_CD_PRODUTO = PDTP_sq.PRME_CD_PRODUTO ");
		sqlSubQuery.append( "	AND (DAUD_sq.DAUD_TX_LOTE = PDTP_sq.PDTP_TX_LOTE ");
		sqlSubQuery.append( "	OR (DAUD_sq.DAUD_TX_LOTE IS NULL AND PDTP_sq.PDTP_TX_LOTE = '') ");
		sqlSubQuery.append( "	) ");
//		sqlSubQuery.append( filtroPorTipoUZ ).append(" ");
		sqlSubQuery.append( "	WHERE OCNF_sq.CONF_SQ_SEQUENCIAL = CONF.CONF_SQ_SEQUENCIAL AND UNTP_sq.PNFT_SQ_SEQUENCIAL = PNFT.PNFT_SQ_SEQUENCIAL ");
		sqlSubQuery.append( filtroPorTipoUZ ).append(" ");
		sqlSubQuery.append( "	GROUP BY ");
		sqlSubQuery.append( "	UNTP_sq.UNTP_NR_UNITIZADOR, ");
		sqlSubQuery.append( "	PRME_sq.PRME_CD_PRODUTO, ");
		sqlSubQuery.append( "	prme_sq.PRME_NR_DV, ");
		sqlSubQuery.append( "	PRME_sq.PRME_TX_DESCRICAO1, ");
		sqlSubQuery.append( "	DAUD_sq.DAUD_QT_UNIT_REC, ");
		sqlSubQuery.append( "	DAUD_sq.DAUD_QT_UNIT_AVARIADA, ");
		sqlSubQuery.append( "	DAUD_sq.DAUD_TX_LOTE, ");
		sqlSubQuery.append( "	PDTP_sq.PDTP_TX_LOTE, ");
		sqlSubQuery.append( "	OCNF_sq.CONF_SQ_SEQUENCIAL "); 
		sqlSubQuery.append( "UNION ");
		sqlSubQuery.append( "SELECT ");
		sqlSubQuery.append( "	UNTP_sq.UNTP_NR_UNITIZADOR 																		AS UNTP_NR_UNITIZADOR, ");
		sqlSubQuery.append( "	PRME_sq.PRME_CD_PRODUTO 																		AS PRME_CD_PRODUTO, ");
		sqlSubQuery.append( "	PRME_sq.PRME_NR_DV 																				AS PRME_NR_DV, ");
		sqlSubQuery.append( "	PRME_sq.PRME_TX_DESCRICAO1 																		AS PRME_TX_DESCRICAO1, ");
		sqlSubQuery.append( "	SUM(CASE WHEN PDTP_sq.PDTP_QT_UNIT_EXPED IS NULL THEN 0 ELSE PDTP_sq.PDTP_QT_UNIT_EXPED END) 	AS QTDE_EXPEDIDA, ");
		sqlSubQuery.append( "	CASE WHEN DAUD_sq.DAUD_QT_UNIT_REC IS NULL THEN 0 ELSE DAUD_sq.DAUD_QT_UNIT_REC END 			AS QTDE_RECEBIDA, ");
		sqlSubQuery.append( "	CASE WHEN DAUD_sq.DAUD_QT_UNIT_AVARIADA IS NULL THEN 0 ELSE DAUD_sq.DAUD_QT_UNIT_AVARIADA END 	AS QTDE_AVARIA, ");
		sqlSubQuery.append( "	CASE WHEN DAUD_sq.DAUD_TX_LOTE IS NULL THEN '' ELSE DAUD_sq.DAUD_TX_LOTE  END 					AS LOTE_CONF, ");
		sqlSubQuery.append( "	CASE WHEN PDTP_sq.PDTP_TX_LOTE IS NULL THEN '' ELSE PDTP_sq.PDTP_TX_LOTE END 					AS LOTE_PDTP, ");
		sqlSubQuery.append( "	OCNF_sq.CONF_SQ_SEQUENCIAL AS CONFERENCIA ");
		sqlSubQuery.append( "	FROM ");
		sqlSubQuery.append( "	COSMOS.DBO.PRODUTO_MESTRE PRME_sq (NOLOCK) ");
		sqlSubQuery.append( "	INNER JOIN PRODUTO_TRANSPORTADO          PDTP_sq (NOLOCK) ON PDTP_sq.PRME_CD_PRODUTO    = PRME_sq.PRME_CD_PRODUTO ");
		sqlSubQuery.append( "	INNER JOIN UNITIZADOR_TRANSPORTADO       UNTP_sq (NOLOCK) ON PDTP_sq.UNTP_SQ_SEQUENCIAL = UNTP_sq.UNTP_SQ_SEQUENCIAL ");
		sqlSubQuery.append( "	INNER JOIN OBJETO_CONFERENCIA_CARGA      OCNF_sq (NOLOCK) ON OCNF_sq.UNTP_SQ_SEQUENCIAL = UNTP_sq.UNTP_SQ_SEQUENCIAL ");
		sqlSubQuery.append( "	INNER JOIN AUDITORIA_UNITIZADOR_CAB      AUDI_sq (NOLOCK) ON AUDI_sq.OCNF_SQ_SEQUENCIAL = OCNF_sq.OCNF_SQ_SEQUENCIAL ");
		sqlSubQuery.append( "	LEFT OUTER JOIN AUDITORIA_UNITIZADOR_DET DAUD_sq (NOLOCK) ON DAUD_sq.AUDI_SQ_SEQUENCIAL = AUDI_sq.AUDI_SQ_SEQUENCIAL ");
		sqlSubQuery.append( "	AND DAUD_sq.PRME_CD_PRODUTO = PRME_sq.PRME_CD_PRODUTO ");
		sqlSubQuery.append( "	AND (DAUD_sq.DAUD_TX_LOTE = PDTP_sq.PDTP_TX_LOTE ");
		sqlSubQuery.append( "		OR (DAUD_sq.DAUD_TX_LOTE IS NULL AND PDTP_sq.PDTP_TX_LOTE = '') ");
		sqlSubQuery.append( ") ");
	//	sqlSubQuery.append( filtroPorTipoUZ ).append(" ");
		sqlSubQuery.append( "	WHERE OCNF_sq.CONF_SQ_SEQUENCIAL = CONF.CONF_SQ_SEQUENCIAL AND UNTP_sq.PNFT_SQ_SEQUENCIAL = PNFT.PNFT_SQ_SEQUENCIAL  ");
		sqlSubQuery.append( filtroPorTipoUZ ).append(" ");
		sqlSubQuery.append( "	GROUP BY ");
		sqlSubQuery.append( "	UNTP_sq.UNTP_NR_UNITIZADOR, ");
		sqlSubQuery.append( "	PRME_sq.PRME_CD_PRODUTO, ");
		sqlSubQuery.append( "	prme_sq.PRME_NR_DV, ");
		sqlSubQuery.append( "	PRME_sq.PRME_TX_DESCRICAO1, ");
		sqlSubQuery.append( "	DAUD_sq.DAUD_QT_UNIT_REC, ");
		sqlSubQuery.append( "	DAUD_sq.DAUD_QT_UNIT_AVARIADA, ");
		sqlSubQuery.append( "	DAUD_sq.DAUD_TX_LOTE, ");
		sqlSubQuery.append( "	PDTP_sq.PDTP_TX_LOTE, ");
		sqlSubQuery.append( "	OCNF_sq.CONF_SQ_SEQUENCIAL ");
		sqlSubQuery.append( "	) AS DIV_AUD_sq ");
		sqlSubQuery.append( "	WHERE DIV_AUD_sq.QTDE_EXPEDIDA <> DIV_AUD_sq.QTDE_RECEBIDA OR DIV_AUD_sq.QTDE_AVARIA > 0 ");
		
		return sqlSubQuery;
	}
	
	/**
	 * Retorna contagem de Itens(registros) que possuem divergência na auditoria
	 * @param filtroPorTipoUZ
	 * @return
	 */
	private StringBuffer sqlSubQueryQtdeAuditoriaItensRealizadaForaDoPrazo( String filtroPorTipoUZ )
	{
		
		filtroPorTipoUZ = filtroPorTipoUZ.replace("UNTPS.", "UNTP_sq.");
		
		StringBuffer sqlSubQuery = new StringBuffer();
		
		sqlSubQuery.append( "SELECT  " );
		sqlSubQuery.append( "	COUNT(1) AS QTD_AUD_FORA_PRZ_sq ");
		sqlSubQuery.append( "FROM (" );
		sqlSubQuery.append( "SELECT " );
		sqlSubQuery.append( "	UNTP_sq.UNTP_NR_UNITIZADOR, " );
		sqlSubQuery.append( "	AUDC_sq.AUDI_DH_INICIO, " );
		sqlSubQuery.append( "	AUDC_sq.AUDI_DH_FIM, " );
		sqlSubQuery.append( "	AUDC_sq.AUDI_FL_OBEDECE_PRAZOAUDIT " );
		sqlSubQuery.append( "FROM " );
		sqlSubQuery.append( "	RCARGA.DBO.AUDITORIA_UNITIZADOR_CAB AS AUDC_sq (NOLOCK) " );
		sqlSubQuery.append( "	INNER JOIN RCARGA.DBO.OBJETO_CONFERENCIA_CARGA AS OCNF_sq (NOLOCK) ON AUDC_sq.OCNF_SQ_SEQUENCIAL = OCNF_sq.OCNF_SQ_SEQUENCIAL " );
		sqlSubQuery.append( "	CROSS JOIN RCARGA.DBO.UNITIZADOR_TRANSPORTADO AS UNTP_sq (NOLOCK) " );
		sqlSubQuery.append( "WHERE " );
		sqlSubQuery.append( "		( AUDC_sq.AUDI_FL_OBEDECE_PRAZOAUDIT = 'I' OR AUDC_sq.AUDI_FL_OBEDECE_PRAZOAUDIT = 'F' ) " );
		sqlSubQuery.append( "	AND ( OCNF_sq.CONF_SQ_SEQUENCIAL = CONF.CONF_SQ_SEQUENCIAL ) " );
		sqlSubQuery.append( "	AND ( OCNF_sq.UNTP_SQ_SEQUENCIAL IN ( UNTP_sq.UNTP_SQ_SEQUENCIAL ) )  AND UNTP_sq.PNFT_SQ_SEQUENCIAL = PNFT.PNFT_SQ_SEQUENCIAL " );
		sqlSubQuery.append( filtroPorTipoUZ ).append(" ");
		sqlSubQuery.append( "GROUP BY " );
		sqlSubQuery.append( "	UNTP_sq.UNTP_NR_UNITIZADOR, " );
		sqlSubQuery.append( "	AUDC_sq.AUDI_DH_INICIO, " );
		sqlSubQuery.append( "	AUDC_sq.AUDI_DH_FIM, " );
		sqlSubQuery.append( "	AUDC_sq.AUDI_FL_OBEDECE_PRAZOAUDIT " );
		sqlSubQuery.append( "	) AS AUD_FORA_PRZ_sq " );
		
		//####
		//sqlSubQuery = new StringBuffer( "select count(1) where 1=1 " );
		
		return sqlSubQuery;
	}
	
	/**
	 * Retorna contagem de Itens(registros) de conferência manual associado à pré-nota
	 * @return
	 */
	private StringBuffer sqlSubQueryQtdeItensConferencialManual( )
	{
		
		StringBuffer sqlSubQuery = new StringBuffer();
		
		sqlSubQuery.append( "SELECT  " );
		sqlSubQuery.append( "	COUNT(1) AS QTD_CONF_MANUAL_sq ");
		sqlSubQuery.append( "FROM " );
		sqlSubQuery.append( "	RCARGA.DBO.CONFERENCIA_MANUAL AS CONM_sq (NOLOCK) " );
		sqlSubQuery.append( "WHERE " );
		sqlSubQuery.append( "	CONM_sq.PNFT_SQ_SEQUENCIAL = PNFT.PNFT_SQ_SEQUENCIAL " );
		
		return sqlSubQuery;
	}

}
