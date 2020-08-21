package br.com.araujo.rastreabilidade.service.rcarga;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import br.com.araujo.rastreabilidade.model.rcarga.dto.ProdutoTransportadoDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.ResultadoDetalheAcompanhamentoDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroDetalheAcompanhamentoDTO;
import br.com.araujo.rastreabilidade.repository.rcarga.dao.DetalheAcompanhamentoCargaDAO;
import br.com.araujo.rastreabilidade.repository.rcarga.dao.ProdutoTransportadoDAO;

@Service
public class DetalheAcompanhamentoCargaService {
	
	@Autowired
	private ProdutoTransportadoDAO produtoTransportadorDAO;

	@Autowired
	private DetalheAcompanhamentoCargaDAO detalheDAO;
	
	public String buscaDetalheUz(Model model, FiltroDetalheAcompanhamentoDTO parametro) throws SQLException {
		boolean indevido = false;
		
		ResultadoDetalheAcompanhamentoDTO resultado = montaResultado(parametro);
		resultado.setDetalhes(detalheDAO.buscaListaDetalhes(parametro));
		
		switch(parametro.getTipo()) {
			case "1": {
				resultado.setTitulo("Detalhe de UZs Expedidos");
				break;
			}
			case "2": {
				resultado.setTitulo("Detalhe de UZs Recebidos");
				break;
			}
			case "3": {
				resultado.setTitulo("Detalhe de UZs Avariados");
				break;
			}
			case "4": {
				resultado.setTitulo("Detalhe de UZs Faltantes");
				break;
			}
			case "5": {
				resultado.setTitulo("Detalhe de UZs Indevidos");
				indevido = true;
				break;
			}
			default: {
				resultado.setTitulo("Detalhe de UZs");
			}
			
		}
		
		model.addAttribute("resultado", resultado);
		
		if (!indevido) {			
			return "consultas/detalhe/uz";
		} else {
			return "consultas/detalhe/uzindevido";
		}
	}


	public String buscaDetalheAuditoria(Model model, FiltroDetalheAcompanhamentoDTO parametro) throws SQLException {
		ResultadoDetalheAcompanhamentoDTO resultado = montaResultado(parametro);
		resultado.setDetalhes(detalheDAO.buscaListaDetalhes(parametro));
		
		switch(parametro.getTipo()) {
			case "6": {
				resultado.setTitulo("Detalhe de Auditoria Realizada");
				break;
			}
			case "7": {
				resultado.setTitulo("Detalhe de Auditoria Não Realizada");
				break;
			}
			default: {
				resultado.setTitulo("Detalhe de Auditoria");
			}
		}
		
		model.addAttribute("resultado", resultado);
		
		return "consultas/detalhe/auditoria";
	}
	

	public String buscaDetalheVolumoso(Model model, FiltroDetalheAcompanhamentoDTO parametro) throws SQLException {
		ResultadoDetalheAcompanhamentoDTO resultado = montaResultado(parametro);
		Integer prenota = Integer.valueOf(parametro.getPrenota());
		List<ProdutoTransportadoDTO> produtos = produtoTransportadorDAO.buscaListaProdutos(prenota);
		
		resultado.setVolumosos(produtos);
		resultado.setTitulo("Detalhe de Volumosos");
		
		montaTotaisProdutos(produtos, resultado);
		
		model.addAttribute("resultado", resultado);
		
		return "consultas/detalhe/volumoso";
	}
	
	private void montaTotaisProdutos(List<ProdutoTransportadoDTO> produtos,
			ResultadoDetalheAcompanhamentoDTO resultado) {
		Integer totalQtdExpedida = 0;
		Integer totalQtdRecebida = 0;
		Integer totalQtdAvariada = 0;
		Integer totalQtdFaltante = 0;
		Integer totalQtdIndevido = 0;
		
		for (ProdutoTransportadoDTO produto : produtos) {
			BigDecimal faltante = new BigDecimal(produto.getSumQtdExpedida() - (produto.getSumQtdRecebida().intValue() + produto.getSumQtdAvariada()));
			produto.setSumQtdFaltante(faltante);
			
			totalQtdAvariada += produto.getSumQtdAvariada();
			totalQtdExpedida += produto.getSumQtdExpedida();
			totalQtdFaltante += produto.getSumQtdFaltante().intValue();
			totalQtdIndevido += produto.getSumQtdIndevido();
			totalQtdRecebida += produto.getSumQtdRecebida().intValue();
		}
		
		resultado.setTotalQtdAvariada(totalQtdAvariada.toString());
		resultado.setTotalQtdExpedida(totalQtdExpedida.toString());
		resultado.setTotalQtdFaltante(totalQtdFaltante.toString());
		resultado.setTotalQtdIndevido(totalQtdIndevido.toString());
		resultado.setTotalQtdRecebida(totalQtdRecebida.toString());
	}


	private ResultadoDetalheAcompanhamentoDTO montaResultado(FiltroDetalheAcompanhamentoDTO parametro)
			throws SQLException {
		ResultadoDetalheAcompanhamentoDTO resultado = new ResultadoDetalheAcompanhamentoDTO();
		resultado.setConferencia(parametro.getConferencia());
		resultado.setPreNota(parametro.getPrenota());
		resultado.setRota(parametro.getRota());
		resultado.setCodFilial(parametro.getCodFilial());
		resultado.setNomeFilial(parametro.getNomeFilial());
		resultado.setOrdemSaida(parametro.getOrdemSaida());
		return resultado;
	}
}
