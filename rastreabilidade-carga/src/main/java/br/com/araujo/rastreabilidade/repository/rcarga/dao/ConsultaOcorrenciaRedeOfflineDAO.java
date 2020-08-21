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
import br.com.araujo.rastreabilidade.model.rcarga.dto.ConsultaOcorrenciaRedeOfflineDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroBaseDTO;

@Repository
public class ConsultaOcorrenciaRedeOfflineDAO {

	@Autowired
	@Qualifier("rcargaEntityManager")
	private EntityManager em;
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<ConsultaOcorrenciaRedeOfflineDTO> buscaListaCancelamentoConferencia(FiltroBaseDTO filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("select filial.FILI_CD_FILIAL as codigoFilial, " +
				   "	filial.FILI_NM_FANTASIA as descricaoFilial, " +
				   " 	FORMAT(controle.CTLG_DH_LOG, 'dd/MM/yyyy') as dataEvento, " +
				   "  	FORMAT(controle.CTLG_DH_LOG, 'hh:mm') as horaEvento, " +
				   "	controle.CTLG_TX_EVENTO as evento " +
				   "  from CONTROLE_LOG_COLETOR controle " +
				   "    join " + AppConstantes.CONTROLE_BANCO + "FILIAL filial on controle.FILI_CD_FILIAL = filial.FILI_CD_FILIAL " +
				   " where ");
		
		if (StringUtils.isNotBlank(filtro.getDataInicial())) {
			hql.append(" controle.CTLG_DH_LOG >= '").append(filtro.getDataInicial()).append(" 00:00:00' ");
		}
		
		if (StringUtils.isNotBlank(filtro.getDataFinal())) {
			hql.append(" and controle.CTLG_DH_LOG <= '").append(filtro.getDataFinal()).append(" 23:59:59' ");				
		}	

		if (filtro.getCodigoFilial() != null) {
			hql.append(" and controle.FILI_CD_FILIAL = ").append(filtro.getCodigoFilial());	
		}
		
		hql.append(" order by controle.CTLG_DH_LOG, controle.FILI_CD_FILIAL asc");
		
		Query query = em.createNativeQuery(hql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(ConsultaOcorrenciaRedeOfflineDTO.class));

		List<ConsultaOcorrenciaRedeOfflineDTO> lista = query.getResultList();
		return lista;
	}

}
