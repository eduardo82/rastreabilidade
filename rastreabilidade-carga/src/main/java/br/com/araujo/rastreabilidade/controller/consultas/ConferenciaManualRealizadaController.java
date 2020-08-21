package br.com.araujo.rastreabilidade.controller.consultas;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.araujo.rastreabilidade.controller.BasePageController;
import br.com.araujo.rastreabilidade.model.rcarga.dto.ConferenciaManualRealizadaDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroConferenciaManualRealizadaDTO;
import br.com.araujo.rastreabilidade.service.rcarga.ConferenciaManualRealizadaService;

@Controller
public class ConferenciaManualRealizadaController extends BasePageController {
	
	@Autowired
	private ConferenciaManualRealizadaService conferenciaManualRealizadaService;

	@GetMapping(value = "conferencia-manual-realizada")
	public String carregaPagina(Model model) {
		return "consultas/conferenciamanualrealizada";
	}
	
	@PostMapping(value = "busca-conferencia-manual")
	@ResponseBody
	public List<ConferenciaManualRealizadaDTO> buscaConferencia(@RequestBody FiltroConferenciaManualRealizadaDTO parametro) {
		return conferenciaManualRealizadaService.buscaListaConferencias(parametro);
	}

	@PostMapping(value = "gravar-conferencia")
	@ResponseBody
	public ResponseEntity<Void> gravaConferencias(HttpServletRequest request, @RequestBody ArrayList<ConferenciaManualRealizadaDTO> conferencias) throws Exception {
		conferenciaManualRealizadaService.gravarConferenciaManual(request, conferencias);
		return ResponseEntity.noContent().build();
	}
}
