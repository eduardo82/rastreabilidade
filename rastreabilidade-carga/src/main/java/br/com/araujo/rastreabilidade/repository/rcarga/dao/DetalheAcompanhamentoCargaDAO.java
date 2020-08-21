package br.com.araujo.rastreabilidade.repository.rcarga.dao;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import br.com.araujo.rastreabilidade.constates.DetalheAcompanhamentoCargaEnum;
import br.com.araujo.rastreabilidade.model.rcarga.dto.UnitizadorTransportadoDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroDetalheAcompanhamentoDTO;

@Repository
public class DetalheAcompanhamentoCargaDAO {
	
	@Autowired
	@Qualifier("rcargaEntityManager")
	private EntityManager em;
	
	@SuppressWarnings({ "unchecked", "static-access" })
	public List<UnitizadorTransportadoDTO> buscaListaDetalhes(FiltroDetalheAcompanhamentoDTO parametro) throws SQLException {
		DetalheAcompanhamentoCargaEnum detalhe = DetalheAcompanhamentoCargaEnum.getDetalhePorCodigo(parametro.getTipo());
		
		String sql = builderCabecalhoSelecao(parametro.getTipo());
		sql += detalhe.getClausulaJoin(parametro.getTipo());
		sql += detalhe.getClausulaWhere(parametro.getTipo());
		sql += builderGroupBy(parametro.getTipo());
		
		Query query = em.createQuery(sql);

		if (detalhe.name().contains("UZ")) {			
			if (!DetalheAcompanhamentoCargaEnum.UZ_INDEVIDO.equals(detalhe)) {			
				query.setParameter("prenota", Integer.valueOf(parametro.getPrenota()));
				
				if (DetalheAcompanhamentoCargaEnum.UZ_RECEBIDO.equals(detalhe) || 
						DetalheAcompanhamentoCargaEnum.UZ_AVARIADO.equals(detalhe)) {
					query.setParameter("conferencia", Integer.valueOf(parametro.getConferencia()));
				}
			} else {
				query.setParameter("conferencia", Integer.valueOf(parametro.getConferencia()));
			}
		} else if (detalhe.name().contains("AUDITORIA")) {
			query.setParameter("prenota", Integer.valueOf(parametro.getPrenota()));
			query.setParameter("conferencia", Integer.valueOf(parametro.getConferencia()));
		}
		
		List<UnitizadorTransportadoDTO> lista = query.getResultList();
		
		return lista;
	}

	private String builderCabecalhoSelecao(String tipo) {
		
		switch(tipo) {
			case "5": {
				return "select new br.com.araujo.rastreabilidade.model.rcarga.dto.UnitizadorTransportadoDTO(" +
						"ocnf.codigoBarraObjetoIndevido, " +
						"ocnf.dataConferencia) " +
						"from ObjetoConferencia ocnf ";
			}
			
			case "6": {
				return "select new br.com.araujo.rastreabilidade.model.rcarga.dto.UnitizadorTransportadoDTO(" +
						"	    pdtp.dataJuliana, " +
						"       pdtp.sequencialDataJuliana, " +
						"       pdtp.tipoProcesso, " +
						"       untp.preNfTransportada.id, " +
						"       untp.id, " +
						"       untp.numeroUnitizador, " +
						"       ocnf.statusUnitizadorVolumoso, " +
						"       ocnf.statusLacre1, " +
						"       ocnf.statusLacre2, " +
						"       untp.tipoUnitizador, " +
						"       (case when (ocnf.statusUnitizadorVolumoso = 'A' or ocnf.statusUnitizadorVolumoso = 'T') then 'AVARIADO |' else '' end " +
						"       + " +
						"       case when (ocnf.statusLacre1 = 'D' or ocnf.statusLacre2 = 'D') then 'LACRE INCORRETO |' " +
						"       	 when (ocnf.statusLacre1 = 'X' or ocnf.statusLacre2 = 'X') then 'LACRE INEXISTENTE |' " +
						"      		 when (ocnf.statusLacre1 = 'R' or ocnf.statusLacre2 = 'R') then 'LACRE ROMPIDO |' else '' end " +
						"       + " +
						"       case when (untp.tipoUnitizador = '00') then '' " +
						"       	 when (untp.tipoUnitizador = '01') then 'CONTROLADO' " +
						"      		 when (untp.tipoUnitizador = '02') then 'MEDICAMENTOS ESPECIAIS' " +
						"      		 when (untp.tipoUnitizador = '03') then 'PAR+' else '' end) as tipoAuditoria, " +
						"       (select count(pdtps.id) " +
						"         	from UnitizadorTransportado untps " +
						"           join untps.produtoTransportado pdtps " +
						"           join untps.objetoConferencia ocnfs  " +
						"           join ocnfs.auditoria audis " +
						"           where untps.id= untp.id " +
						"          )" +
						"    as totalItens,  " +
						"       (select sum(pdtps.quantidadeExpedida) " +
						"         	from UnitizadorTransportado untps " +
						"           join untps.produtoTransportado pdtps " +
						"           join untps.objetoConferencia ocnfs  " +
						"           join ocnfs.auditoria audis " +
						"           where untps.id= untp.id " +
						"          )" +
						"    as quantidadeExpedida,  " +
						"       count(daud.codigoProduto) as totalItensAuditados, " +
						"       sum(daud.quantidadeUnitariaRecebida - daud.quantidadeUnitariaAvariada) as quantidadeExpedidaSemProblema, " +
						"       sum(daud.quantidadeUnitariaAvariada) as quantidadeExpedidaAvariada )" +
						" from UnitizadorTransportado untp " +
						"      join untp.preNfTransportada pre " +
						"      join untp.produtoTransportado pdtp " +
						"      join untp.objetoConferencia ocnf " +
						"      join ocnf.auditoria audi " +
						"      join audi.detalheAuditoria daud ";
			}
			case "7": {				
				return "select new br.com.araujo.rastreabilidade.model.rcarga.dto.UnitizadorTransportadoDTO(" +
						"	    pdtp.dataJuliana, " +
						"       pdtp.sequencialDataJuliana, " +
						"       pdtp.tipoProcesso, " +
						"       untp.preNfTransportada.id, " +
						"       untp.id, " +
						"       untp.numeroUnitizador, " +
						"       ocnf.statusUnitizadorVolumoso, " +
						"       ocnf.statusLacre1, " +
						"       ocnf.statusLacre2, " +
						"       untp.tipoUnitizador, " +
						"       (case when (ocnf.statusUnitizadorVolumoso = 'A' or ocnf.statusUnitizadorVolumoso = 'T') then 'AVARIADO |' else '' end " +
						"       + " +
						"       case when (ocnf.statusLacre1 = 'D' or ocnf.statusLacre2 = 'D') then 'LACRE INCORRETO |' " +
						"       	 when (ocnf.statusLacre1 = 'X' or ocnf.statusLacre2 = 'X') then 'LACRE INEXISTENTE |' " +
						"      		 when (ocnf.statusLacre1 = 'R' or ocnf.statusLacre2 = 'R') then 'LACRE ROMPIDO |' else '' end " +
						"       + " +
						"       case when (untp.tipoUnitizador = '00') then '' " +
						"       	 when (untp.tipoUnitizador = '01') then 'CONTROLADO' " +
						"      		 when (untp.tipoUnitizador = '02') then 'MEDICAMENTOS ESPECIAIS' " +
						"      		 when (untp.tipoUnitizador = '03') then 'PAR+' else '' end) as tipoAuditoria, " +
						"       count(pdtp.id) as totalItens, " +
						"       sum(pdtp.quantidadeExpedida) as quantidadeExpedida) " +
						" from UnitizadorTransportado untp " +
						"      join untp.preNfTransportada pre " +
						"      join untp.produtoTransportado pdtp " +
						"      join untp.objetoConferencia ocnf " +
						"      left outer join ocnf.auditoria audi ";
			}
			default: {
				return "select new br.com.araujo.rastreabilidade.model.rcarga.dto.UnitizadorTransportadoDTO( " +
					 	"pdtp.dataJuliana, " +
					 	"pdtp.sequencialDataJuliana, " +
					 	"pdtp.tipoProcesso, " +
					 	"untp.preNfTransportada.id, " +
					 	"untp.id, " +
						"untp.numeroUnitizador, " +
						"untp.tipoUnitizador, " +
						"count(pdtp.produto) as totalItens, " +
						"sum(pdtp.quantidadeExpedida) as quantidadeExpedida ) " +
						"from  UnitizadorTransportado untp ";
			}
				
		}
	}
	
	private String builderGroupBy(String tipo) {
		switch(tipo) {
			case "5": {
				return "";
			}
			
			case "6": {
				
			}
			case "7": {	
				return " group by pdtp.dataJuliana, " +
						"       pdtp.sequencialDataJuliana, " +
						"       pdtp.tipoProcesso, " +
						"       untp.preNfTransportada.id, " +
						"       untp.id, " +
						"       untp.numeroUnitizador, " +
						"       ocnf.statusUnitizadorVolumoso, " +
						"       ocnf.statusLacre1, " +
						"       ocnf.statusLacre2, " +
						"       untp.tipoUnitizador " +
						"order by untp.numeroUnitizador ";
			}
			
			default: {
				return 	"group by " +
						"pdtp.dataJuliana, " +
						"pdtp.sequencialDataJuliana, " +
						"pdtp.tipoProcesso, " +
						"untp.preNfTransportada.id, " +
						"untp.id, " +
						"untp.numeroUnitizador, " +
						"untp.tipoUnitizador " +
						"order by untp.numeroUnitizador asc";
			}
		}
	}

}
