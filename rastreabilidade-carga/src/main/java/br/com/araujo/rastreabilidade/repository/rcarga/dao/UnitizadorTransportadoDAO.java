package br.com.araujo.rastreabilidade.repository.rcarga.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import br.com.araujo.rastreabilidade.constates.AppConstantes;
import br.com.araujo.rastreabilidade.model.rcarga.dto.EntregaNaoRealizadaDTO;

@Repository
public class UnitizadorTransportadoDAO {

	@Autowired
	@Qualifier("rcargaEntityManager")
	private EntityManager em;
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<EntregaNaoRealizadaDTO> buscaEntregaNaoRealizadasUz(Date dataRotaEntrega, Integer rota) {

		String sql = "select " +
		        " rota.ROTA_DT_ROTAENTREGA as dataRotaEntrega, " +
		        " rota.ROTA_SQ_SEQUENCIAL as rotaAcompanhamento, " +
		        " fili.FILI_CD_FILIAL as codigoFilial, " +
		        " fili.FILI_NM_FANTASIA as nomeFilial, " + 
		        " 'UZ' as tipoTransportado, " +
		        " untp.UNTP_NR_UNITIZADOR as numeroUnitizador, " +
		        " untp.UNTP_NR_UNITIZADOR as descricaoTransportado " +
		  		" FROM UNITIZADOR_TRANSPORTADO untp (NOLOCK)" +
		  		" JOIN PRE_NF_TRANSPORTADA pnft (NOLOCK) on untp.PNFT_SQ_SEQUENCIAL = pnft.PNFT_SQ_SEQUENCIAL " +
		  		" JOIN " + AppConstantes.CONTROLE_BANCO + "ROTA rota (NOLOCK) on pnft.ROTA_SQ_SEQUENCIAL = rota.ROTA_SQ_SEQUENCIAL and pnft.ROTA_DT_ROTAENTREGA  = rota.ROTA_DT_ROTAENTREGA " +
		  		" JOIN " + AppConstantes.CONTROLE_BANCO + "FILIAL fili (NOLOCK) on pnft.FILI_CD_FILIAL = fili.FILI_CD_FILIAL " +
		  		" LEFT OUTER JOIN OBJETO_CONFERENCIA_CARGA ocnf (NOLOCK) on untp.UNTP_SQ_SEQUENCIAL = ocnf.UNTP_SQ_SEQUENCIAL " +
		  		" where ocnf.PDTP_SQ_SEQUENCIAL is null " +
		  		" and ocnf.CONF_SQ_SEQUENCIAL is null " +
		  		" and rota.ROTA_DT_ROTAENTREGA = :dataRotaEntrega ";
		
		if (rota != null) {
			sql += " and rota.ROTA_SQ_SEQUENCIAL = :rota";
		}
	
		Query query = em.createNativeQuery(sql).unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(EntregaNaoRealizadaDTO.class));
		
		query.setParameter("dataRotaEntrega", dataRotaEntrega);
		
		if (rota != null) {
			query.setParameter("rota", rota);	
		}
	
		List<EntregaNaoRealizadaDTO> lista = query.getResultList();
	
		return lista;
	}
}
