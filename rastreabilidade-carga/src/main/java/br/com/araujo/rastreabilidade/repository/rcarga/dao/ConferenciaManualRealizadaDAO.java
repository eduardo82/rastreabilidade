package br.com.araujo.rastreabilidade.repository.rcarga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import br.com.araujo.rastreabilidade.constates.AppConstantes;
import br.com.araujo.rastreabilidade.model.rcarga.dto.ConferenciaManualRealizadaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroConferenciaManualRealizadaDTO;

@Repository
public class ConferenciaManualRealizadaDAO {

	@Autowired
	@Qualifier("rcargaEntityManager")
	private EntityManager em;
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<ConferenciaManualRealizadaDTO> buscaListaConferencia(FiltroConferenciaManualRealizadaDTO parametro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("select distinct ");
		hql.append("pnft.PNFT_SQ_SEQUENCIAL as codigoPrenotaFiscalTransportada, ");
		hql.append("pnft.PNFC_SQ_SEQUENCIAL as codigoPrenota, ");
		hql.append("pnft.FILI_CD_FILIAL	as codigoFilial, ");
		hql.append("fili.FILI_NM_FANTASIA as nomeFilial, ");
		hql.append("pnft.PNFT_DH_INCLUSAO as dataInclusao, ");
		hql.append("pnft.PNFT_DH_EXPORT as dataExportacao, ");
		hql.append("pdtp.ITNF_TP_PROCESSO as tipoProcesso, ");
		hql.append("pdtp.ITNF_DT_JULARM as dataJuliana, ");
		hql.append("pdtp.ITNF_SQ_DTJULARM as sequencialDataJuliana, ");
		hql.append("pnft.CONF_SQ_SEQUENCIAL as codigoConferencia, ");
		hql.append("comr.CONM_SQ_SEQUENCIAL as id, ");
		hql.append("comr.CONM_FL_EXCCARGA as flagExclusaoCarga, ");
		hql.append("conf.CONF_FL_TIPO as tipoConferencia, ");
		hql.append("conf.CONF_FL_STATUS as statusConferencia ");
		hql.append("FROM PRE_NF_TRANSPORTADA pnft (NOLOCK) ");	
		hql.append("JOIN PRODUTO_TRANSPORTADO pdtp (NOLOCK) on pnft.PNFT_SQ_SEQUENCIAL = pdtp.PNFT_SQ_SEQUENCIAL ");
		hql.append("JOIN " + AppConstantes.CONTROLE_BANCO + "FILIAL fili (NOLOCK) on pnft.FILI_CD_FILIAL = fili.FILI_CD_FILIAL ");
		hql.append("LEFT OUTER JOIN CONFERENCIA_CARGA conf (NOLOCK) on pnft.CONF_SQ_SEQUENCIAL = conf.CONF_SQ_SEQUENCIAL ");
		hql.append("LEFT OUTER JOIN CONFERENCIA_MANUAL comr (NOLOCK) on pnft.PNFT_SQ_SEQUENCIAL = comr.PNFT_SQ_SEQUENCIAL ");
		hql.append("WHERE pnft.ROTA_DT_ROTAENTREGA >= :dataInclusaoIni	");
		hql.append("AND pnft.ROTA_DT_ROTAENTREGA < :dataInclusaoFim	");
		hql.append("AND comr.CONM_SQ_SEQUENCIAL is null ");
		hql.append("AND conf.CONF_SQ_SEQUENCIAL is null ");
		
		if (StringUtils.isNotBlank(parametro.getCodigoFilial())) {
			hql.append(" and pnft.FILI_CD_FILIAL = :filial ");	
		}
		
		if ("S".equals(parametro.getConferenciaRealizada())) {
			hql.append(" and comr.CONM_SQ_SEQUENCIAL is not null ");
		}
		
		hql.append(" ORDER BY pnft.FILI_CD_FILIAL, pnft.PNFT_DH_INCLUSAO, pdtp.ITNF_DT_JULARM, pdtp.ITNF_SQ_DTJULARM" );
		
		Query query = em.createNativeQuery(hql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(ConferenciaManualRealizadaDTO.class));
			
		query.setParameter("dataInclusaoIni", parametro.getDataInicial());
		query.setParameter("dataInclusaoFim", parametro.getDataFinal());
		
		if (StringUtils.isNotBlank(parametro.getCodigoFilial())) {
			query.setParameter("filial", parametro.getCodigoFilial());
		}
		
		List<ConferenciaManualRealizadaDTO> lista = query.getResultList();
		return lista;
	}
}
