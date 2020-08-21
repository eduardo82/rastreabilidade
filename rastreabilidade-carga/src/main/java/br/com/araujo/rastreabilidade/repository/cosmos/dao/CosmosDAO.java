package br.com.araujo.rastreabilidade.repository.cosmos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class CosmosDAO {

	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	public String buscaNomeImpressora(String ip) {
		Query query = em.createNativeQuery("SELECT MAPI_TX_PATHIMPR FROM COSMOS.DBO.MAPA_IMPRESSORA"
				+ " WHERE MAPI_TX_IPCLIENTE = '" + ip + "'");
		
		List<String> impressoras = query.getResultList();
		return impressoras != null && impressoras.size() > 0 ? impressoras.get(0) : "";
	}
}
