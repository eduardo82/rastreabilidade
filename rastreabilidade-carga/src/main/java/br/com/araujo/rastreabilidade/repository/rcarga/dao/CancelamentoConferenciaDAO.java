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
import br.com.araujo.rastreabilidade.model.rcarga.dto.ConferenciaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroBaseDTO;

@Repository
public class CancelamentoConferenciaDAO {

	@Autowired
	@Qualifier("rcargaEntityManager")
	private EntityManager em;
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<ConferenciaDTO> buscaListaCancelamentoConferencia(FiltroBaseDTO filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("select distinct filial.FILI_CD_FILIAL as codigoFilial, " +
				   "	filial.FILI_NM_FANTASIA as nomeFilial, " +
				   " 	conf.CONF_SQ_SEQUENCIAL as id, " +
				   " 	FORMAT(conf.CONF_DH_ABERTURA, 'dd/MM/yyyy') as dataAberturaView, " +
				   "  	conf.CONF_CD_USU_ABERT as cdUsuAbert, " +
				   "    conf.CONF_CD_USU_TRANSP_ABERT as responsavelTransporteAbertura, " +
				   "	FORMAT(conf.CONF_DH_CANCELAMENTO, 'dd/MM/yyyy') as dataCancelamentoView, " +
				   "	conf.CONF_CD_USUCANCEL as responsavelCancelamento" +
				   "  from PRE_NF_CONFERENCIA_CANCELADA pncc " +
				   " 	join CONFERENCIA_CARGA conf on pncc.CONF_SQ_SEQUENCIAL = conf.CONF_SQ_SEQUENCIAL " +
				   " 	join PRE_NF_TRANSPORTADA pnft on pncc.PNFT_SQ_SEQUENCIAL = pnft.PNFT_SQ_SEQUENCIAL " +
				   "    join " + AppConstantes.CONTROLE_BANCO + "FILIAL filial on pnft.FILI_CD_FILIAL = filial.FILI_CD_FILIAL " +
				   " where conf.CONF_FL_STATUS = 'C' ");
		
		if (filtro.getCodigoFilial() != null) {
			hql.append(" and filial.FILI_CD_FILIAL = ").append(filtro.getCodigoFilial());	
		}
		
		if (StringUtils.isNotBlank(filtro.getDataInicial())) {
			hql.append(" and conf.CONF_DH_ABERTURA >= '").append(filtro.getDataInicial()).append("' ");
		}
		
		if (StringUtils.isNotBlank(filtro.getDataFinal())) {
			hql.append(" and conf.CONF_DH_ABERTURA <= '").append(filtro.getDataFinal()).append("' ");				
		}	
		
		if (filtro.getRota() != null) {
			hql.append(" and pnft.ROTA_SQ_SEQUENCIAL =  ").append(filtro.getRota());				
		}
		
		Query query = em.createNativeQuery(hql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(ConferenciaDTO.class));

		List<ConferenciaDTO> lista = query.getResultList();
		return lista;
	}
}
