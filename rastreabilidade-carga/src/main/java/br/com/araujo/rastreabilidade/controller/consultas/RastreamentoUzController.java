package br.com.araujo.rastreabilidade.controller.consultas;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.araujo.rastreabilidade.controller.BasePageController;
import br.com.araujo.rastreabilidade.model.rcarga.dto.FiltroRastreamentoUzDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.RastreamentoUzDTO;
import br.com.araujo.rastreabilidade.service.rcarga.RastreamentoUzService;

@Controller
public class RastreamentoUzController extends BasePageController {
	
	@Autowired
	private RastreamentoUzService service;

	@GetMapping(value = "rastreamento-uz")
	public String carregaPagina(Model model) {
		model.addAttribute("filtros", service.montaFiltrosTela());
		model.addAttribute("cabecalhos", service.montaCabecalhos());
	
		return "consultas/rastreamentouz";
	}
	
	@PostMapping(value = "busca-rastreamento-uz", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<RastreamentoUzDTO> buscaRastreamentoUz(@RequestBody FiltroRastreamentoUzDTO filtro) {
		return service.buscaListaRastreamentoUz(filtro);
	}

}
