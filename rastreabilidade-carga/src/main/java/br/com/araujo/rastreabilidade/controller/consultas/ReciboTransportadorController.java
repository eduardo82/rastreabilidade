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
import br.com.araujo.rastreabilidade.model.rcarga.dto.ReciboTransportadorDTO;
import br.com.araujo.rastreabilidade.model.rcarga.dto.filtro.FiltroConferenciaDTO;
import br.com.araujo.rastreabilidade.service.rcarga.ReciboTransportadorService;

@Controller
public class ReciboTransportadorController extends BasePageController {
	
	@Autowired
	private ReciboTransportadorService service;

	@GetMapping(value = "recibo-transportador")
	public String carregaPagina(Model model) {
		return "consultas/recibotransportador";
	}
	
	@PostMapping(value = "busca-recibo-transportador")
	@ResponseBody
	public List<ReciboTransportadorDTO> buscaReciboTransportador(HttpServletRequest request, 
			@RequestBody FiltroConferenciaDTO filtro) throws ParseException {
		return service.buscaConferencia(request, filtro);
	}
	
	@PostMapping(value = "gerar-relatorio-recibo", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Void> imprimirRelatorio(HttpServletRequest request, @RequestBody FiltroConferenciaDTO filtro) throws FileNotFoundException, PrintException, ParseException {
		service.gerarRelatorio(request, filtro);
		
		return ResponseEntity.noContent().build();
	}

}
