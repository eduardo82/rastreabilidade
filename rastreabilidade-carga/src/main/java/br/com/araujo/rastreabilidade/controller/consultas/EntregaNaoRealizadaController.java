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
import br.com.araujo.rastreabilidade.model.rcarga.dto.EntregaNaoRealizadaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroEntregaNaoRealizadaDTO;
import br.com.araujo.rastreabilidade.service.rcarga.EntregaNaoRealizadaService;

@Controller
public class EntregaNaoRealizadaController extends BasePageController {
	
	@Autowired
	private EntregaNaoRealizadaService service;

	@GetMapping(value = "entregas-nao-realizadas")
	public String carregaPagina(Model model) {
		model.addAttribute("filtros", service.montaFiltrosTela());
		model.addAttribute("cabecalhos", service.montaCabecalhos());

		return "consultas/entregasnaorealizadas";
	}
	
	@PostMapping(value = "busca-entregas-nao-realizadas", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<EntregaNaoRealizadaDTO> buscaEntregasNaoRealizada(@RequestBody FiltroEntregaNaoRealizadaDTO filtro) {
		return service.buscaEntregasNaoRealizadas(filtro);
	}
}
