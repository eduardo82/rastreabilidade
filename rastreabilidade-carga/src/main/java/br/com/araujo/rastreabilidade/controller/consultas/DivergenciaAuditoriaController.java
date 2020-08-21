package br.com.araujo.rastreabilidade.controller.consultas;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.araujo.rastreabilidade.controller.BasePageController;
import br.com.araujo.rastreabilidade.model.rcarga.dto.DivergenciaAuditoriaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.comum.IdValuePaginadoDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroDivergenciaAuditoriaDTO;
import br.com.araujo.rastreabilidade.service.rcarga.DivergenciaAuditoriaService;

@Controller
public class DivergenciaAuditoriaController extends BasePageController {
	
	@Autowired
	private DivergenciaAuditoriaService service;

	@GetMapping(value = "divergencia-auditoria")
	public String carregaPagina(Model model) {
		return "consultas/divergencia-auditoria";
	}
	
	@PostMapping(value = "busca-divergencia-auditoria", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<DivergenciaAuditoriaDTO> buscaDivergenciaAuditoria(@RequestBody FiltroDivergenciaAuditoriaDTO filtro) {
		return service.buscaDivergenciaAuditoria(filtro);
	}
	
	@GetMapping(value = "clusters")
	public String buscaClusters(Model model) {
		model.addAttribute("registros", service.listaClusters());
		return "comuns/busca-registros";
	}
	
	@GetMapping(value = "descricao-cluster")
	@ResponseBody
	public String buscaDescricaoCluster(@RequestParam("id") String id) {
		
		if (StringUtils.isNotBlank(id)) {			
			return service.buscaDescricaoCluster(Integer.valueOf(id));
		}
		return null;
	}	

	@GetMapping(value = "produtos")
	public String carregaTelaProdutos(Model model, HttpServletRequest request) {
		return "comuns/busca-registros-paginada";
	}
	
	@GetMapping(value = "produto-paginado")
	@ResponseBody
	public IdValuePaginadoDTO buscaProdutos(Model model, HttpServletRequest request) {
		return service.listaProdutos(request);
	}
	
	@GetMapping(value = "descricao-produto")
	@ResponseBody
	public String buscaDescricaoProduto(@RequestParam("id") String id) {
		
		if (StringUtils.isNotBlank(id)) {			
			return service.buscaDescricaoProduto(Integer.valueOf(id));
		}
		return null;
	}
	
	@GetMapping(value = "detalhe-divergencia-auditoria")
	public String detalheDivergenciaProduto(Model model, FiltroDivergenciaAuditoriaDTO filtro) {
		List<DivergenciaAuditoriaDTO> lista = service.recuperaDetalheDivergenciaProduto(filtro);
		model.addAttribute("produto", filtro);
		model.addAttribute("quantidadeTotal", service.calculaQuantidadeTotal(lista));
		model.addAttribute("divergencias", lista);
		return "consultas/detalhe/detalhe-divergencias-produto";
	}

}
