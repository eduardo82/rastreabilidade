package br.com.araujo.rastreabilidade.repository.rcarga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import br.com.araujo.rastreabilidade.constates.AppConstantes;
import br.com.araujo.rastreabilidade.model.rcarga.dto.ConferenciaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroConferenciaDTO;

@Repository
public class FechamentoConferenciaDAO {

	@PersistenceContext
	private EntityManager em;
	
	public List<ConferenciaDTO> buscaConferencias(FiltroConferenciaDTO filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("select distinct new br.com.araujo.rastreabilidade.model.rcarga.dto.ConferenciaDTO(obj.id, obj.dataAbertura, obj.dataFechamento, " +
				"obj.dataFechamentoParcial, pnft.filial, '', obj.dataImpressao, " +
				"obj.dataReimpressao) from ConferenciaCarga obj, PreNfTransportada pnft " +
				"where obj.id = pnft.conferencia.id ");
	
		if (StringUtils.isNotBlank(filtro.getSituacao())) {					
			if (AppConstantes.FILTROS_PESQUISA.SITUACAO_FECHADO.equals(filtro.getSituacao())){
				hql.append(" and obj.dataFechamento is not null");
			}
			else if (AppConstantes.FILTROS_PESQUISA.SITUACAO_EM_ABERTO.equals(filtro.getSituacao())){
				hql.append(" and obj.dataAbertura is not null and obj.dataFechamento is null");
			}
			
			else if (AppConstantes.FILTROS_PESQUISA.SITUACAO_PARCIALMENTE_FECHADO.equals(filtro.getSituacao())){
				hql.append("and obj.dataFechamentoParcial is not null and obj.dataFechamento is null");
			}
			else if (AppConstantes.FILTROS_PESQUISA.SITUACAO_AMBOS.equals(filtro.getSituacao())) {				
				hql.append(" and obj.flagStatus != 'C' ");						
			}
		} else{
			hql.append(" and obj.flagStatus != 'C' ");	
		}
		
		if (StringUtils.isNotBlank(filtro.getTipoImpressao())) {
			if (AppConstantes.FILTROS_PESQUISA.TIPO_IMPRESSAO_IMPRESSO.equals(filtro.getTipoImpressao())) {				
				hql.append(" and obj.dataImpressao is not null ");
			}
			
			if (AppConstantes.FILTROS_PESQUISA.TIPO_IMPRESSAO_NAOIMPRESSO.equals(filtro.getTipoImpressao())) {				
				hql.append(" and obj.dataImpressao is null and obj.dataReimpressao is null ");
			}
		}
		
		if (StringUtils.isNotBlank(filtro.getDataInicial()) && (StringUtils.isBlank(filtro.getSituacao()) 
				|| StringUtils.isBlank(filtro.getTipoImpressao())))
			hql.append(" and obj.dataAbertura >= '").append(filtro.getDataInicial()).append("' and obj.dataAbertura " +
					"<= '").append(filtro.getDataFinal() + "'");
			
		else if (StringUtils.isNotBlank(filtro.getDataInicial()) && (StringUtils.isNotBlank(filtro.getSituacao()) 
				|| StringUtils.isNotBlank(filtro.getTipoImpressao())))
			hql.append(" and obj.dataAbertura >= '").append(filtro.getDataInicial()).append("' and obj.dataAbertura " +
					"<= '").append(filtro.getDataFinal() + "'");
			
		if (filtro.getCodigoFilial() != null) {			
			hql.append(" and pnft.filial = " + filtro.getCodigoFilial());
		}
		
		if (StringUtils.isNotBlank(filtro.getCodigoDeposito())) {			
			hql.append(" and pnft.codigoDeposito = " + filtro.getCodigoDeposito());
		}
		
		return em.createQuery(hql.toString(), ConferenciaDTO.class).getResultList();
	}
}
