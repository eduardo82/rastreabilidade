package br.com.araujo.rastreabilidade.controller.consultas;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

import javax.print.PrintException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.araujo.rastreabilidade.controller.BasePageController;
import br.com.araujo.rastreabilidade.model.rcarga.dto.ConferenciaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroConferenciaDTO;
import br.com.araujo.rastreabilidade.service.rcarga.FechamentoConferenciaService;

@Controller
public class FechamentoConferenciaController extends BasePageController {
	
	@Autowired
	private FechamentoConferenciaService service;
	
	@GetMapping(value = "fechamento-conferencia")
	public String carregaPagina(Model model) {
		model.addAttribute("filtros", service.montaFiltrosTela());
		model.addAttribute("cabecalhos", service.montaCabecalhos());
		return "consultas/fechamentoconferencia";
	}
	
	@PostMapping(value = "busca-conferencias", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ConferenciaDTO> buscaConferencias(HttpServletRequest request, @RequestBody FiltroConferenciaDTO filtro) {
		return service.buscaConferencias(request, filtro);
	}
	
	@PostMapping(value = "gerar-relatorio", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Void> imprimirRelatorio(HttpServletRequest request, @RequestBody FiltroConferenciaDTO filtro) throws FileNotFoundException, PrintException, ParseException {
		service.gerarRelatorio(request, filtro);
		
		return ResponseEntity.noContent().build();
	}
}