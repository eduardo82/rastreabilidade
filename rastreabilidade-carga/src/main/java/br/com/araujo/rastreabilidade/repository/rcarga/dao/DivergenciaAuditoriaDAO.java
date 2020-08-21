package br.com.araujo.rastreabilidade.repository.rcarga.dao;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import br.com.araujo.rastreabilidade.constates.AppConstantes;
import br.com.araujo.rastreabilidade.constates.TipoSimNao;
import br.com.araujo.rastreabilidade.model.rcarga.dto.DivergenciaAuditoriaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.comum.IdValueDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroDivergenciaAuditoriaDTO;
import br.com.araujo.rastreabilidade.utils.DateUtils;

@Repository
public class DivergenciaAuditoriaDAO {

	@Autowired
	@Qualifier("rcargaEntityManager")
	private EntityManager em;
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<IdValueDTO> listaProdutos(Integer numeroPagina, Integer tamanhoPagina, String descricao) {
		String hql = "select PRME_CD_PRODUTO as id, PRME_TX_DESCRICAO1 as descricao "
				+ " from " + AppConstantes.CONTROLE_BANCO + "PRODUTO_MESTRE ";
		
		if (StringUtils.isNotBlank(descricao)) {
			hql += " WHERE PRME_TX_DESCRICAO1 like '%" + descricao + "%'";
		}
		
		hql += " order by PRME_TX_DESCRICAO1 asc";
		
		Query query = em.createNativeQuery(hql)
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(IdValueDTO.class));

		query.setFirstResult(numeroPagina);
		query.setMaxResults(tamanhoPagina);
		List<IdValueDTO> lista = query.getResultList();

		return lista;
	}
	
	public Integer countProdutos(String descricao) {
		String hql = "select PRME_CD_PRODUTO"
				+ " from " + AppConstantes.CONTROLE_BANCO + "PRODUTO_MESTRE ";
		
		if (StringUtils.isNotBlank(descricao)) {
			hql += " WHERE PRME_TX_DESCRICAO1 like '%" + descricao + "%'";
		}
		
		Query query = em.createNativeQuery(hql);
		
		return query.getResultList().size();
	}

	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<IdValueDTO> listaClusters() {
		String hql = "select CAST(canal.CANA_SQ_CANAL as int) as id, canal.CANA_DS_CANAL as descricao "
				+ " from " + AppConstantes.CONTROLE_BANCO + "CANAL canal "
				+ " order by canal.CANA_DS_CANAL asc";
		
		Query query = em.createNativeQuery(hql)
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(IdValueDTO.class));

		List<IdValueDTO> lista = query.getResultList();
		return lista;
	}

	
	@SuppressWarnings("deprecation")
	public String buscaDescricaoCluster(Integer id) {
		try {			
			String hql = "select canal.CANA_DS_CANAL as descricao "
					+ " from " + AppConstantes.CONTROLE_BANCO + "CANAL canal "
					+ " where canal.CANA_SQ_CANAL = " + id;
			
			Query query = em.createNativeQuery(hql)
					.unwrap(org.hibernate.query.Query.class)
					.setResultTransformer(new AliasToBeanResultTransformer(IdValueDTO.class));
			
			IdValueDTO cluster = (IdValueDTO) query.getSingleResult();
			
			return cluster != null ? cluster.getDescricao() : "";
		} catch (Exception e) {
			return "";
		}
	}

	@SuppressWarnings("deprecation")
	public String buscaDescricaoProduto(Integer id) {
		try {			
			String hql = "select PRME_TX_DESCRICAO1 as descricao "
					+ " from " + AppConstantes.CONTROLE_BANCO + "PRODUTO_MESTRE "
					+ " where PRME_CD_PRODUTO = " + id;
			
			Query query = em.createNativeQuery(hql)
					.unwrap(org.hibernate.query.Query.class)
					.setResultTransformer(new AliasToBeanResultTransformer(IdValueDTO.class));
			
			IdValueDTO produto = (IdValueDTO) query.getSingleResult();
			
			return produto != null ? produto.getDescricao() : "";
		} catch (Exception e) {
			return "";
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<DivergenciaAuditoriaDTO> buscaDivergenciaAuditoria(FiltroDivergenciaAuditoriaDTO filtro) {
		StringBuilder hql = new StringBuilder();
		hql.append("select ");
		hql.append("	obj.codigoProduto as codigoProduto, ");
		hql.append("	obj.descricaoProduto as descricaoProduto, ");
		hql.append("	sum(obj.quantidadeDivergenciaAvariada) as quantidadeDivergenciaAvariada, ");
		hql.append("	sum(obj.quantidadeDivergenciaFalta) as quantidadeDivergenciaFalta, ");
		hql.append("	sum(obj.quantidadeDivergenciaLote) as quantidadeDivergenciaLote, ");
		hql.append("	sum(obj.quantidadeDivergenciaExcesso) as quantidadeDivergenciaExcesso, ");
		hql.append("		(sum(obj.quantidadeDivergenciaAvariada) + ");
		hql.append("		sum(obj.quantidadeDivergenciaFalta) + ");
		hql.append("		sum(obj.quantidadeDivergenciaLote) + ");
		hql.append("		sum(obj.quantidadeDivergenciaExcesso)) ");
		hql.append("	as quantidadeDivergenciaTotal, ");
		hql.append("	obj.digitoProduto as digitoProduto ");
		hql.append("from VwDivergenciaAuditConf obj ");
		hql.append("where obj.dataInicio >= :dtInicio ");
		hql.append("and obj.dataFim <= :dtFim ");
		
		if (filtro.getCodigoCluster() != null){
			hql.append("and obj.codigoClusterAprev = :cdClusterAprev ");
		}
		
		if (StringUtils.isNotBlank(filtro.getTipoUzVolumoso()) && !"T".equals(filtro.getTipoUzVolumoso())) {
			hql.append("and obj.tipo = :tipo ");
		}
		
		if (StringUtils.isNotBlank(filtro.getTipoUz()) && !"T".equals(filtro.getTipoUz())) {
			hql.append("and obj.tipoUZ = :tipoUz ");
		}
		
		if (filtro.getRota() != null){
			hql.append("and obj.idRota = :idRota ");
		}
		
		if (filtro.getCodigoFilial() != null) {
			hql.append("and obj.filial = :idFilial ");
		}
		
		if (filtro.getCodigoProduto() != null) {
			hql.append("and obj.codigoProduto = :cdProduto ");
		}
		
		hql.append("group by obj.codigoProduto, obj.descricaoProduto, obj.digitoProduto, obj.dataInicio ");
		
		if (StringUtils.isNotBlank(filtro.getTipoDivergencia()) && !"T".equals(filtro.getTipoDivergencia())){
			hql.append(this.getHavingTipoDivergencia(filtro.getTipoDivergencia()));
		}
		
		if (StringUtils.isNotBlank(filtro.getOrderBy())) {
			hql.append("order by ".concat(filtro.getOrderBy()).concat(" "));
		}
		else{
			hql.append("order by obj.descricaoProduto, obj.codigoProduto ");
		}
		
		Query query = em.createQuery(hql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(DivergenciaAuditoriaDTO.class));
		
		query.setParameter("dtInicio", DateUtils.parseOrNull(filtro.getDataInicial(), DateUtils.FORMATO_DATA_WEB));
		query.setParameter("dtFim", DateUtils.parseFinalDiaOrNull(filtro.getDataFinal(), DateUtils.FORMATO_DATA_WEB));
		
		if (filtro.getCodigoCluster() != null){
			query.setParameter("cdClusterAprev", filtro.getCodigoCluster());
		}
		
		if (StringUtils.isNotBlank(filtro.getTipoUzVolumoso()) && !"T".equals(filtro.getTipoUzVolumoso())){
			query.setParameter("tipo", filtro.getTipoUzVolumoso());
		}
		
		if (StringUtils.isNotBlank(filtro.getTipoUz()) && !"T".equals(filtro.getTipoUz())){
			query.setParameter("tipoUz", filtro.getTipoUz());
		}
		
		if (filtro.getRota() != null) {
			query.setParameter("idRota", filtro.getRota());
		}
		
		if (filtro.getCodigoFilial() != null) {
			query.setParameter("idFilial", filtro.getCodigoFilial());
		}
		
		if (filtro.getCodigoProduto() != null) {
			query.setParameter("cdProduto", filtro.getCodigoProduto());
		}
		
		List<DivergenciaAuditoriaDTO> divergencias = (List<DivergenciaAuditoriaDTO>) query.getResultList();

		if (TipoSimNao.S.getSigla().equals(filtro.getAntibiotico())) {
			divergencias = divergencias.stream()
			.filter(divergencia -> verificaAntibiotico(divergencia.getCodigoProduto()))
			.collect(Collectors.toList());
		}
		
		
		return divergencias;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<DivergenciaAuditoriaDTO> recuperaDetalheDivergenciaProduto(FiltroDivergenciaAuditoriaDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select ");
		hql.append("	fili.FILI_CD_FILIAL as codigoFilial, ");
		hql.append("	fili.FILI_NM_FANTASIA as descricaoFilial, ");
		hql.append("	sum(obj.QTDE_DIV_AVARIADA) as quantidadeAvariadaNative, ");
		hql.append("	sum(obj.QTDE_DIV_FALTA) as quantidadeFaltaNative, ");
		hql.append("	sum(obj.QTDE_DIV_LOTE) as quantidadeLoteNative, ");
		hql.append("	sum(obj.QTDE_DIV_EXCESSO) as quantidadeExcessoNative, ");
		hql.append("	(sum(obj.QTDE_DIV_AVARIADA) + ");
		hql.append("		sum(obj.QTDE_DIV_FALTA) + ");
		hql.append("		sum(obj.QTDE_DIV_LOTE) + ");
		hql.append("		sum(obj.QTDE_DIV_EXCESSO)) ");
		hql.append("	as quantidadeTotalNative, ");
		hql.append("	obj.PRME_CD_PRODUTO as codigoProduto, ");
		hql.append("	obj.PRME_NR_DV as digitoProdutoNative ");
		hql.append("from VW_DIVERGENCIA_AUDIT_CONF obj ");
		hql.append("join " + AppConstantes.CONTROLE_BANCO + "filial fili on obj.FILI_CD_FILIAL = fili.FILI_CD_FILIAL ");
		hql.append("where obj.PRME_CD_PRODUTO = :cdProduto ");
		hql.append("and obj.DATA_INICIO >= :dtInicio ");
		hql.append("and obj.DATA_FIM <= :dtFim ");
		
		if (filtro.getCodigoCluster() != null){
			hql.append("and obj.CLUSTER_APREV = :cdClusterAprev ");
		}
		
		if (StringUtils.isNotBlank(filtro.getTipoUzVolumoso()) && !"T".equals(filtro.getTipoUzVolumoso())) {
			hql.append("and obj.TIPO = :tipo ");
		}
		
		if (StringUtils.isNotBlank(filtro.getTipoUz()) && !"T".equals(filtro.getTipoUz())) {
			hql.append("and obj.TIPO_UZ_AUDITORIA = :tipoUz ");
		}
		
		if (filtro.getRota() != null){
			hql.append("and obj.ROTA_SQ_SEQUENCIAL = :idRota ");
		}
		
		if (filtro.getCodigoFilial() != null) {
			hql.append("and obj.FILI_CD_FILIAL = :idFilial ");
		}
		
		hql.append("group by fili.FILI_CD_FILIAL, fili.FILI_NM_FANTASIA, obj.PRME_CD_PRODUTO, obj.PRME_NR_DV ");
		
		if (StringUtils.isNotBlank(filtro.getTipoDivergencia()) && !"T".equals(filtro.getTipoDivergencia())){
			hql.append(this.getHavingTipoDivergenciaNative(filtro.getTipoDivergencia()));
		}
		
		hql.append("order by fili.FILI_CD_FILIAL ");
		
		Query query = em.createNativeQuery(hql.toString())
				.unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(DivergenciaAuditoriaDTO.class));
	
		query.setParameter("cdProduto", filtro.getCodigoProdutoDetalhe());
		query.setParameter("dtInicio", DateUtils.parseOrNull(filtro.getDataInicial(), DateUtils.FORMATO_DATA_WEB));
		query.setParameter("dtFim", DateUtils.parseFinalDiaOrNull(filtro.getDataFinal(), DateUtils.FORMATO_DATA_WEB));

		if (filtro.getCodigoCluster() != null){
			query.setParameter("cdClusterAprev", filtro.getCodigoCluster());
		}
		
		if (StringUtils.isNotBlank(filtro.getTipoUzVolumoso()) && !"T".equals(filtro.getTipoUzVolumoso())){
			query.setParameter("tipo", filtro.getTipoUzVolumoso());
		}
		
		if (StringUtils.isNotBlank(filtro.getTipoUz()) && !"T".equals(filtro.getTipoUz())){
			query.setParameter("tipoUz", filtro.getTipoUz());
		}
		
		if (filtro.getRota() != null) {
			query.setParameter("idRota", filtro.getRota());
		}
		
		if (filtro.getCodigoFilial() != null) {
			query.setParameter("idFilial", filtro.getCodigoFilial());
		}

		List<DivergenciaAuditoriaDTO> divergencias = (List<DivergenciaAuditoriaDTO>) query.getResultList();
		
		return divergencias;
	}
	
	/**
	 * Retorna a clausula having correspondente ao tipo de divergencia
	 * @param tipoDivergencia
	 * @since 17/03/2011
	 * @author sidney.machado
	 */
	private String getHavingTipoDivergencia(String tipoDivergencia) {
		switch (tipoDivergencia) {
		case "A":
			return "having sum(obj.quantidadeDivergenciaAvariada) > 0 ";
		case "F":
			return "having sum(obj.quantidadeDivergenciaFalta) > 0 ";
		case "D":
			return "having sum(obj.quantidadeDivergenciaLote) > 0 ";
		case "E":
			return "having sum(obj.quantidadeDivergenciaExcesso) > 0 ";
		default:
			break;
		}
		return StringUtils.EMPTY;
	}
	
	
	/**
	 * Retorna a clausula having correspondente ao tipo de divergencia
	 * @param tipoDivergencia
	 * @since 17/03/2011
	 * @author sidney.machado
	 */
	private String getHavingTipoDivergenciaNative(String tipoDivergencia) {
		switch (tipoDivergencia) {
		case "A":
			return "having sum(obj.QTDE_DIV_AVARIADA) > 0 ";
		case "F":
			return "having sum(obj.QTDE_DIV_FALTA) > 0 ";
		case "D":
			return "having sum(obj.QTDE_DIV_LOTE) > 0 ";
		case "E":
			return "having sum(obj.QTDE_DIV_EXCESSO) > 0 ";
		default:
			break;
		}
		return StringUtils.EMPTY;
	}
	
	private boolean verificaAntibiotico(Integer codigoProduto) {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" set dateformat dmy SELECT PRME_CD_PRODUTO ");
		sql.append(" FROM [COSMOS].[dbo].[fc_CsmPsicotropico_Antibioticos] "); 
		sql.append(" ( CONVERT( VARCHAR, GETDATE(), 103), CONVERT( VARCHAR, GETDATE(), 103) ) WHERE PRME_CD_PRODUTO = " + codigoProduto);
		
		Query query = em.createNativeQuery(sql.toString());
		
		return query.getResultList() != null;
	}
}
