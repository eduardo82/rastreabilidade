package br.com.araujo.rastreabilidade.service.rcarga;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.araujo.rastreabilidade.model.rcarga.dto.DivergenciaAuditoriaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.comum.IdValueDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.comum.IdValuePaginadoDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroDivergenciaAuditoriaDTO;
import br.com.araujo.rastreabilidade.repository.rcarga.dao.DivergenciaAuditoriaDAO;

@Service
public class DivergenciaAuditoriaService {
	
	private static final String DRAW = "draw";
	private static final String START = "start";
	private static final String SEARCH = "search[value]";
	
	@Autowired
	private DivergenciaAuditoriaDAO repositoryDAO;
	
	public List<IdValueDTO> listaClusters() {
		return repositoryDAO.listaClusters();
	}

	public String buscaDescricaoCluster(Integer valueOf) {
		return repositoryDAO.buscaDescricaoCluster(valueOf);
	}

	public IdValuePaginadoDTO listaProdutos(HttpServletRequest request) {
		List<IdValueDTO> produtos = new ArrayList<IdValueDTO>();
		Integer inicio = 0;
		Integer draw = 1;
		Integer totalProdutos = 0;
	
		if (StringUtils.isNotBlank(request.getParameter(DRAW))) {
			inicio = Integer.valueOf(request.getParameter(START));
			draw = Integer.valueOf(request.getParameter(DRAW));
		}
		
		String produto = request.getParameter(SEARCH);
		
		if (StringUtils.isNotBlank(produto)) {
			produtos = repositoryDAO.listaProdutos(inicio, 10, produto);
			totalProdutos = repositoryDAO.countProdutos(produto);
		} else {
			produtos = repositoryDAO.listaProdutos(inicio, 10, null);	
			totalProdutos = repositoryDAO.countProdutos(null);
		}
		
		IdValuePaginadoDTO idPaginado = new IdValuePaginadoDTO();
		idPaginado.setData(produtos);
		idPaginado.setDraw(draw);
		idPaginado.setRecordsTotal(totalProdutos);
		idPaginado.setRecordsFiltered(totalProdutos);
		
		return idPaginado;
	}

	public String buscaDescricaoProduto(Integer valueOf) {
		return repositoryDAO.buscaDescricaoProduto(valueOf);
	}

	public List<DivergenciaAuditoriaDTO> buscaDivergenciaAuditoria(FiltroDivergenciaAuditoriaDTO filtro) {
		return repositoryDAO.buscaDivergenciaAuditoria(filtro);
	}

	public List<DivergenciaAuditoriaDTO> recuperaDetalheDivergenciaProduto(FiltroDivergenciaAuditoriaDTO filtro) {
		return repositoryDAO.recuperaDetalheDivergenciaProduto(filtro);
	}

	public Integer calculaQuantidadeTotal(List<DivergenciaAuditoriaDTO> lista) {
		return lista.stream()
				.mapToInt(detalhe -> detalhe.getQuantidadeTotalNative().intValue())
				.sum();
	}

}
