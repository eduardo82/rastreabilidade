package br.com.araujo.rastreabilidade.repository.rcarga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import br.com.araujo.rastreabilidade.constates.AppConstantes;
import br.com.araujo.rastreabilidade.model.rcarga.dto.FiltroRastreamentoUzDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.RastreamentoUzDTO;

@Repository
public class RastreamentoUzDAO {

	@Autowired
	@Qualifier("rcargaEntityManager")
	private EntityManager em;
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<RastreamentoUzDTO> buscaRastreamentoUz(FiltroRastreamentoUzDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT CASE WHEN UNTP.UNTP_NR_UNITIZADOR IS NULL THEN OCNF.OCNF_CD_BARRA_INDEVIDO " +
				"ELSE UNTP.UNTP_NR_UNITIZADOR END AS uz, " +
				"FILI.FILI_CD_FILIAL AS codigoFilial, ");
		sql.append("FILI.FILI_NM_FANTASIA AS descricaoFilial, OCNF.OCNF_ST_UNITIZADOR_VOLUMOSO AS situacao, OCNF.OCNF_DH_CONFERENCIA AS dataHora ");
		sql.append("FROM OBJETO_CONFERENCIA_CARGA AS OCNF (NOLOCK) INNER JOIN ");
		sql.append("PRE_NF_TRANSPORTADA AS PNFT (NOLOCK)  ON OCNF.CONF_SQ_SEQUENCIAL =  PNFT.CONF_SQ_SEQUENCIAL ");
		sql.append("LEFT JOIN UNITIZADOR_TRANSPORTADO AS UNTP  (NOLOCK)  ");
		sql.append("ON UNTP.UNTP_SQ_SEQUENCIAL = OCNF.UNTP_SQ_SEQUENCIAL ");
		sql.append("INNER JOIN " + AppConstantes.CONTROLE_BANCO+"FILIAL AS FILI  (NOLOCK) ");
		sql.append("ON PNFT.FILI_CD_FILIAL = FILI.FILI_CD_FILIAL ");
		sql.append("WHERE (UNTP.UNTP_NR_UNITIZADOR = :codigoUnitizador OR OCNF.OCNF_CD_BARRA_INDEVIDO = :codigoUnitizador) ");
		sql.append("AND ");
		sql.append("(OCNF.OCNF_DH_CONFERENCIA BETWEEN :dataInicial AND :dataFinal) ");
		sql.append("ORDER BY OCNF.OCNF_DH_CONFERENCIA DESC");
		
		Query query = em.createNativeQuery(sql.toString()).unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(RastreamentoUzDTO.class));
		
		query.setParameter("codigoUnitizador", filtro.getCodigoUnitizador());
		query.setParameter("dataInicial", filtro.getDataInicial());
		query.setParameter("dataFinal", filtro.getDataFinal());
		
		List<RastreamentoUzDTO> lista = query.getResultList();
	
		return lista;
		
	}
}
