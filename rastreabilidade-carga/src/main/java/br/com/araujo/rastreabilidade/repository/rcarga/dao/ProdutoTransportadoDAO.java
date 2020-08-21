package br.com.araujo.rastreabilidade.repository.rcarga.dao;

import java.sql.SQLException;
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
import br.com.araujo.rastreabilidade.model.rcarga.dto.ProdutoTransportadoDTO;

@Repository
public class ProdutoTransportadoDAO {

	@Autowired
	@Qualifier("rcargaEntityManager")
	private EntityManager em;

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<ProdutoTransportadoDTO> buscaListaProdutos(Integer prenota) throws SQLException {
		String sql = "select " + "pdtp.ITNF_DT_JULARM as dataJuliana,"
				+ "pdtp.ITNF_SQ_DTJULARM as sequencialDataJuliana," 
				+ "pdtp.ITNF_TP_PROCESSO as tipoProcesso,"
				+ "pdtp.PNFT_SQ_SEQUENCIAL as prenota," 
				+ "ocnf.PDTP_SQ_SEQUENCIAL as id,"
				+ "PRME.PRME_CD_PRODUTO as codigoProduto," 
				+ "PRME.PRME_NR_DV as digitoProduto,"
				+ "PRME.PRME_TX_DESCRICAO1 as descricao," 
				+ "pdtp.PDTP_SG_EMBEXP as siglaEmbalagem,"
				+ "pdtp.PDTP_QT_EMBEXP as quantidadeEmbalagem,"
				+ "ISNULL(sum(pdtp.PDTP_QT_UNIT_EXPED),0) as sumQtdExpedida,"
				+ "ISNULL(sum(ocnf.OCNF_QT_UNIT_REC) - sum(ocnf.OCNF_QT_UNIT_AVARIADA) - sum(ocnf.OCNF_QT_UNIT_INDEVIDOS), 0) as sumQtdRecebida,"
				+ "ISNULL(sum(ocnf.OCNF_QT_UNIT_AVARIADA), 0) as sumQtdAvariada,"
				+ "ISNULL(sum(ocnf.OCNF_QT_UNIT_INDEVIDOS), 0) as sumQtdIndevido "
				+ "from PRODUTO_TRANSPORTADO (NOLOCK) pdtp  " 
				+ "INNER JOIN " + AppConstantes.CONTROLE_BANCO + "PRODUTO_MESTRE PRME (NOLOCK) ON PRME.PRME_CD_PRODUTO = PDTP.PRME_CD_PRODUTO "
				+ "LEFT JOIN OBJETO_CONFERENCIA_CARGA ocnf (NOLOCK) ON pdtp.PDTP_SQ_SEQUENCIAL = ocnf.PDTP_SQ_SEQUENCIAL "
				+ "where pdtp.PNFT_SQ_SEQUENCIAL = :prenota and " 
				+ "pdtp.UNTP_SQ_SEQUENCIAL is null and "
				+ "pdtp.PDTP_SQ_SEQUENCIAL is not null " 
				+ "group by pdtp.ITNF_DT_JULARM," 
				+ "pdtp.ITNF_SQ_DTJULARM,"
				+ "pdtp.ITNF_TP_PROCESSO," 
				+ "pdtp.PNFT_SQ_SEQUENCIAL," 
				+ "ocnf.PDTP_SQ_SEQUENCIAL,"
				+ "PRME.PRME_CD_PRODUTO," 
				+ "PRME.PRME_NR_DV," 
				+ "PRME.PRME_TX_DESCRICAO1," 
				+ "pdtp.PDTP_SG_EMBEXP,"
				+ "pdtp.PDTP_QT_EMBEXP " 
				+ "order by PRME.PRME_TX_DESCRICAO1";

		Query query = em.createNativeQuery(sql).unwrap(org.hibernate.query.Query.class)
				.setResultTransformer(new AliasToBeanResultTransformer(ProdutoTransportadoDTO.class));
		query.setParameter("prenota", prenota);

		List<ProdutoTransportadoDTO> lista = query.getResultList();

		return lista;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<EntregaNaoRealizadaDTO> buscaEntregaNaoRealizadasVolumosos(Date dataRotaEntrega, Integer rota) {
		
		String sql = "select " +
				" rota.ROTA_DT_ROTAENTREGA as dataRotaEntrega, " +
			    " rota.ROTA_SQ_SEQUENCIAL as rotaAcompanhamento, " +
			    " fili.FILI_CD_FILIAL as codigoFilial, " +
			    " fili.FILI_NM_FANTASIA as nomeFilial, " +  
		        " 'Volumoso' as tipoTransportado, " +
		        " prme.PRME_CD_PRODUTO as codigoProduto, " +
		        " prme.PRME_TX_DESCRICAO1 as descricaoProduto, " +
		        " pdtp.PDTP_SG_EMBEXP as siglaEmbalagemExpedida, " + 
		        " pdtp.PDTP_QT_EMBEXP as qtdeEmbalagemExpedida, " + 
		        " pdtp.PDTP_QT_UNIT_EXPED as qtdeUnitariaExpedida, " +
		        " prme.PRME_CD_PRODUTO + ' - ' + prme.PRME_TX_DESCRICAO1 as descricaoTransportado" +
		  		" FROM PRODUTO_TRANSPORTADO pdtp (NOLOCK) " +
		  		" JOIN " + AppConstantes.CONTROLE_BANCO + "PRODUTO_MESTRE prme (NOLOCK) on pdtp.PRME_CD_PRODUTO = prme.PRME_CD_PRODUTO " +
		  		" JOIN PRE_NF_TRANSPORTADA pnft (NOLOCK) on pdtp.PNFT_SQ_SEQUENCIAL = pnft.PNFT_SQ_SEQUENCIAL " +
		  		" JOIN " + AppConstantes.CONTROLE_BANCO + "ROTA rota (NOLOCK) on pnft.ROTA_SQ_SEQUENCIAL = rota.ROTA_SQ_SEQUENCIAL and pnft.ROTA_DT_ROTAENTREGA  = rota.ROTA_DT_ROTAENTREGA " +
		  		" JOIN " + AppConstantes.CONTROLE_BANCO + "FILIAL fili (NOLOCK) on pnft.FILI_CD_FILIAL = fili.FILI_CD_FILIAL " +
		  		" LEFT OUTER JOIN OBJETO_CONFERENCIA_CARGA ocnf (NOLOCK) on pdtp.UNTP_SQ_SEQUENCIAL = ocnf.UNTP_SQ_SEQUENCIAL " +
		  		" where pdtp.UNTP_SQ_SEQUENCIAL is null " +
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
