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
import br.com.araujo.rastreabilidade.model.rcarga.dto.ConferenciaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroBaseDTO;
import br.com.araujo.rastreabilidade.service.rcarga.CancelamentoConferenciaService;

@Controller
public class CancelamentoConferenciaController extends BasePageController {

	@Autowired
	private CancelamentoConferenciaService service; 
	
	@GetMapping(value = "cancelamento-conferencia")
	public String carregaPagina(Model model) {
		model.addAttribute("filtros", service.montaFiltrosTela());
		model.addAttribute("cabecalhos", service.montaCabecalhos());

		return "consultas/cancelamento-conferencia";
	}
	
	@PostMapping(value = "busca-lista-cancelamento-conferencia", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ConferenciaDTO> buscaListaCancelamentoConferencia(@RequestBody FiltroBaseDTO filtro) {
		return service.buscaListaCancelamentoConferencia(filtro);
	}

}
