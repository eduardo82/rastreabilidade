package br.com.araujo.rastreabilidade.repository.rcarga;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.araujo.rastreabilidade.model.rcarga.PreNfTransportada;

@Repository
public interface ReexportarNotaFiscalRepository extends JpaRepository<PreNfTransportada, Integer> {

	@Query(value = "SELECT " + 
			"		nft.PNFT_SQ_SEQUENCIAL, " + 
			"		nft.PNFT_DH_EXPORT, " + 
			"		usu.USUA_NM_USUARIO " + 
			"		FROM PRE_NF_TRANSPORTADA nft " + 
			"		LEFT JOIN COSMOS.dbo.USUARIO usu ON nft.PNFT_CD_USU_EXPORT = usu.USUA_TX_MATRICULA " + 
			"		WHERE nft.FILI_CD_FILIAL = :idFilial " + 
			"		AND nft.ROTA_DT_ROTAENTREGA = :dataRotaEntrega " + 
			"		AND nft.PNFT_DH_EXPORT is not null " + 
			"		AND nft.PNFT_CD_USU_EXPORT is NOT null ",
			nativeQuery = true)
	List<Object[]> pesquisaPreNotaTransportada(@Param("idFilial") Long idFilial, 
			  @Param("dataRotaEntrega") Date dataRota);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE PreNfTransportada " +
		" set dataExportacao = null, cdUsuExport = null " +
		" where id in :idPreNF ")
	void atualizaPreNFTransportadaReexportar(@Param("idPreNF") List<Integer> idPreNF);
}
